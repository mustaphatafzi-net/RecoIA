package com.recoia.auth.mapper;

import com.recoia.auth.dto.UserDTO;
import com.recoia.auth.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl {

    public UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public User fromUserDTO(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
