package com.ead.authuser.security;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserModel userModel = this.userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with username: " + username)
                );

        return UserDetailsImpl.build(userModel);
    }

}
