package com.myinterview.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myinterview.dto.RegisterRequest;
import com.myinterview.exception.UserAlreadyExistsException;
import com.myinterview.model.User;
import com.myinterview.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(RegisterRequest request) {
        repository.findByEmail(request.getEmail())
                .ifPresent(u -> { throw new UserAlreadyExistsException("Email already registered"); });

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));

        return repository.save(user);
    }
}
