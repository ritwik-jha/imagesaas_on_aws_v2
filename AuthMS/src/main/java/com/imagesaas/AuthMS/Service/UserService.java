package com.imagesaas.AuthMS.Service;

import com.imagesaas.AuthMS.Entity.ResponseVO;
import com.imagesaas.AuthMS.Entity.UsersDto;

public interface UserService {
    public ResponseVO registerUser(UsersDto user);

    public ResponseVO loginUser(UsersDto user);
}
