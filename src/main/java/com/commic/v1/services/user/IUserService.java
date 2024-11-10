package com.commic.v1.services.user;

import com.commic.v1.dto.responses.UserResponse;

public interface IUserService {
    public UserResponse getUserInfo(String username);
}
