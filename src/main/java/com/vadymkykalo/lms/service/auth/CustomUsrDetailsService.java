package com.vadymkykalo.lms.service.auth;

import com.vadymkykalo.lms.entity.User;

import com.vadymkykalo.lms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
public class CustomUsrDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user= userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + email));
        return new CustomUserDetails(user);
    }
}
