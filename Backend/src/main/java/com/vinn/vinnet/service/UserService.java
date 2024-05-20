package com.vinn.vinnet.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vinn.vinnet.dto.UserDto;

@Service
public interface UserService {
    boolean createUser(UserDto userDto) throws GeneralSecurityException, IOException;
    UserDto getUserByName(String name);
    UserDto getUserByEmail(String email);
    List<UserDto> getUsersByName(String name);
    UserDto getUserById(long id);
}
