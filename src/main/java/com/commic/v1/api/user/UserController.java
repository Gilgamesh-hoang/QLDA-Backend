package com.commic.v1.api.user;

import com.commic.v1.dto.responses.UserResponse;
import com.commic.v1.jwt.JwtTokenUtil;
import com.commic.v1.services.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getUserInfo(@RequestParam("token") String token) {
        UserResponse user = userService.getUserInfo(jwtTokenUtil.extractUsername(token));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

}
