package com.DatLeo.BookShop.service.impl;

import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailCustom(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username/password không đúng. Vui lòng kiểm tra lại!");
        }

        return new CustomUserDetails(user);
    }
}
