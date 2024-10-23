

package com.ecommerce.user.service;


import com.ecommerce.user.exception.InvalidCredentialsException;
import com.ecommerce.user.exception.UserAlreadyExistsException;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    public User loginUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return existingUser;
    }
}

