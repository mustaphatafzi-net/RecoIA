
package com.recoia.auth.service;

import com.recoia.auth.dto.*;
import com.recoia.auth.exception.InvalidCredentialsException;
import com.recoia.auth.exception.UserAlreadyExistsException;
import com.recoia.auth.exception.UserNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthService {

    // Authentication
    UserDTO register(RegisterRequestDTO registerRequestDTO)
            throws UserAlreadyExistsException;

    AuthResponseDTO login(LoginRequestDTO loginRequestDTO)
            throws InvalidCredentialsException;

    // User management
    UserDTO getUser(Long userId)
            throws UserNotFoundException;

    Page<UserDTO> getAllUsers(Pageable pageable);

    UserDTO updateUser(Long userId, UpdateUserRequestDTO request)
            throws UserNotFoundException, UserAlreadyExistsException;

    void disableUser(Long userId)
            throws UserNotFoundException;

    void enableUser(Long userId)
            throws UserNotFoundException;

    void deleteUser(Long userId)
            throws UserNotFoundException;

    // Admin management
    UserDTO createFirstAdmin(CreateAdminRequestDTO request)
            throws UserAlreadyExistsException;

    UserDTO createAdmin(CreateAdminRequestDTO request)
            throws UserAlreadyExistsException;
}
