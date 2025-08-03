package com.imagesaas.AuthMS.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imagesaas.AuthMS.Entity.ResponseVO;
import com.imagesaas.AuthMS.Entity.UsersDto;
import com.imagesaas.AuthMS.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {
    
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public String getMethodName(@RequestParam String param) {
        return "hello " + param;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseVO> registerUser(@RequestBody UsersDto users){
        if(users.getEmail().strip() == "" || users.getEmail() == null || users.getPassword().strip() == "" || users.getPassword() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVO(false, "Email and password required", users));
        }

        ResponseVO res = userService.registerUser(users);

        if(res.isStatus()){
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseVO> loginUser(@RequestBody UsersDto users){
        if(users.getEmail().strip() == "" || users.getEmail() == null || users.getPassword().strip() == "" || users.getPassword() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVO(false, "Email and password required", users));
        }

        ResponseVO res = userService.loginUser(users);

        if(res.isStatus()){
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
    
}
