package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.dto.request.ReqAddItemCart;
import com.DatLeo.BookShop.dto.response.ResUserCartDTO;
import com.DatLeo.BookShop.entity.Book;
import com.DatLeo.BookShop.entity.Cart;
import com.DatLeo.BookShop.entity.CartDetail;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.exception.ApiException;
import com.DatLeo.BookShop.exception.ApiMessage;
import com.DatLeo.BookShop.repository.BookRepository;
import com.DatLeo.BookShop.repository.CartDetailRepository;
import com.DatLeo.BookShop.repository.CartRepository;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.BookService;
import com.DatLeo.BookShop.service.CartDetailService;
import com.DatLeo.BookShop.service.CartService;
import com.DatLeo.BookShop.service.UserService;
import com.DatLeo.BookShop.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartDetailService cartDetailService;
    private final SecurityUtil  securityUtil;

    @Override
    public void handleAddCart(ReqAddItemCart req) {

        Integer userId = securityUtil.getIdCurrentUserLogin();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ApiMessage.ID_USER_NOT_EXIST));

        Book book = bookRepository.findById(req.getBookId())
                .orElseThrow(() -> new ApiException(ApiMessage.BOOK_NOT_EXIST));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Optional<CartDetail> existedDetail = cartDetailRepository.findByCartIdAndBookId(cart.getId(), book.getId());

        if (existedDetail.isPresent()) {
            CartDetail cartDetail = existedDetail.get();
            cartDetail.setQuantity(cartDetail.getQuantity() + req.getQuantity());
            cartDetailRepository.save(cartDetail);
        } else {
            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setCart(cart);
            newCartDetail.setBook(book);
            newCartDetail.setQuantity(req.getQuantity());
            newCartDetail.setPrice(book.getPrice());
            cartDetailRepository.save(newCartDetail);
        }

        cartRepository.save(cart);
    }

    @Override
    public ResUserCartDTO handleGetCart() {

        Integer userId = securityUtil.getIdCurrentUserLogin();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ApiMessage.ID_USER_NOT_EXIST));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        List<CartDetail> cartDetails = cart.getCartDetails();
        if (cartDetails == null) cartDetails = new ArrayList<>();

        ResUserCartDTO res = new ResUserCartDTO();
        res.setUserId(userId);
        res.setCartId(cart.getId());
        res.setTotalBooks(cartDetails.size());

        List<ResUserCartDTO.BookDetail> resBooks = cartDetails.stream().map(item -> {
            try {
                return cartDetailService.convertToBookDetail(item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
        res.setBooks(resBooks);
        return res;
    }
}
