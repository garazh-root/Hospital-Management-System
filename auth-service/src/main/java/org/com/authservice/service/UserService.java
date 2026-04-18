package org.com.authservice.service;

import lombok.RequiredArgsConstructor;
import org.com.authservice.additional.AuthServiceMessages;
import org.com.authservice.model.User;
import org.com.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(AuthServiceMessages.USER_NOT_FOUND.getMessage())
        );
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(AuthServiceMessages.USER_NOT_FOUND.getMessage())
        );
    }
}