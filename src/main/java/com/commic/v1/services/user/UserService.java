package com.commic.v1.services.user;

import com.commic.v1.dto.responses.UserResponse;
import com.commic.v1.entities.User;
import com.commic.v1.mapper.UserMapper;
import com.commic.v1.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserResponse getUserInfo(String username) {
        // Find the user by username. If the user is not found, return null.
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }

        // Map the User entity to UserResponse DTO
        UserResponse result = userMapper.toDTO(user);

        // Return the result
        return result;
    }
}
