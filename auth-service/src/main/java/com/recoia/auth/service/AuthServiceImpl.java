
package com.recoia.auth.service;

import com.recoia.auth.dto.*;
import com.recoia.auth.entity.User;
import com.recoia.auth.enums.Role;
import com.recoia.auth.exception.InvalidCredentialsException;
import com.recoia.auth.exception.UserAlreadyExistsException;
import com.recoia.auth.exception.UserNotFoundException;
import com.recoia.auth.mapper.UserMapperImpl;
import com.recoia.auth.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private UserMapperImpl userMapper;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    // ==================== AUTHENTICATION ====================

    @Override
    public UserDTO register(RegisterRequestDTO request)
            throws UserAlreadyExistsException {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.STUDENT);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return userMapper.fromUser(savedUser);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request)
            throws InvalidCredentialsException {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new InvalidCredentialsException(
                    "User account is disabled"
            );
        }

        user.setLastLoginAt(LocalDateTime.now());

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        UserDTO userDTO = userMapper.fromUser(user);

        return new AuthResponseDTO(userDTO, token);
    }

    // ==================== USER MANAGEMENT ====================

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUser(Long userId)
            throws UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        return userMapper.fromUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(userMapper::fromUser);
    }

    @Override
    public UserDTO updateUser(Long userId, UpdateUserRequestDTO request)
            throws UserNotFoundException, UserAlreadyExistsException {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        if (request.getEmail() != null
                && !request.getEmail().equalsIgnoreCase(user.getEmail())) {

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException(
                        "Email already exists"
                );
            }

            user.setEmail(request.getEmail());
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        User updatedUser = userRepository.save(user);

        return userMapper.fromUser(updatedUser);
    }

    @Override
    public void disableUser(Long userId)
            throws UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        user.setEnabled(false);

        userRepository.save(user);
    }

    @Override
    public void enableUser(Long userId)
            throws UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId)
            throws UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        userRepository.delete(user);
    }

    // ==================== ADMIN MANAGEMENT ====================

    @Override
    public UserDTO createFirstAdmin(CreateAdminRequestDTO request)
            throws UserAlreadyExistsException {

        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new UserAlreadyExistsException(
                    "An administrator already exists"
            );
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(
                    "Email already exists"
            );
        }

        User admin = new User();

        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        admin.setCreatedAt(LocalDateTime.now());

        User savedAdmin = userRepository.save(admin);

        return userMapper.fromUser(savedAdmin);
    }

    @Override
    public UserDTO createAdmin(CreateAdminRequestDTO request)
            throws UserAlreadyExistsException {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User admin = new User();

        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        admin.setCreatedAt(LocalDateTime.now());

        User savedAdmin = userRepository.save(admin);

        return userMapper.fromUser(savedAdmin);
    }
}
