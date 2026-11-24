
package com.recoia.auth.controller;

import com.recoia.auth.dto.*;
import com.recoia.auth.exception.InvalidCredentialsException;
import com.recoia.auth.exception.UserAlreadyExistsException;
import com.recoia.auth.exception.UserNotFoundException;
import com.recoia.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthRestController {

    private final AuthService authService;

    @Value("${admin.bootstrap-secret}")
    private String adminBootstrapSecret;

    // ==================== AUTHENTICATION ====================

    @PostMapping("/auth/register")
    public UserDTO register(
            @RequestBody RegisterRequestDTO request)
            throws UserAlreadyExistsException {

        return authService.register(request);
    }

    @PostMapping("/auth/login")
    public AuthResponseDTO login(
            @RequestBody LoginRequestDTO request)
            throws InvalidCredentialsException {

        return authService.login(request);
    }

    // ==================== USER MANAGEMENT ====================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/auth/users")
    public Page<UserDTO> getAllUsers(
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {

        return authService.getAllUsers(pageable);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and @userSecurity.isSelf(#p0))")
    @GetMapping("/auth/users/{id}")
    public UserDTO getUser(@PathVariable("id") Long id)
            throws UserNotFoundException {

        return authService.getUser(id);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and @userSecurity.isSelf(#p0))")
    @PutMapping("/auth/users/{id}")
    public UserDTO updateUser(
            @PathVariable("id") Long id,
            @RequestBody UpdateUserRequestDTO request)
            throws UserNotFoundException, UserAlreadyExistsException {

        return authService.updateUser(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/auth/users/{id}/disable")
    public void disableUser(@PathVariable("id") Long id)
            throws UserNotFoundException {

        authService.disableUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/auth/users/{id}/enable")
    public void enableUser(@PathVariable("id") Long id)
            throws UserNotFoundException {

        authService.enableUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/auth/users/{id}")
    public void deleteUser(@PathVariable("id") Long id)
            throws UserNotFoundException {

        authService.deleteUser(id);
    }

    // ==================== ADMIN MANAGEMENT ====================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/auth/admins")
    public UserDTO createAdmin(
            @RequestBody CreateAdminRequestDTO request)
            throws UserAlreadyExistsException {

        return authService.createAdmin(request);
    }

    @PostMapping("/auth/bootstrap/admin")
    public UserDTO createFirstAdmin(
            @RequestHeader("X-Admin-Bootstrap-Key") String bootstrapKey,
            @RequestBody CreateAdminRequestDTO request)
            throws UserAlreadyExistsException {

        boolean validKey = MessageDigest.isEqual(
                adminBootstrapSecret.getBytes(StandardCharsets.UTF_8),
                bootstrapKey.getBytes(StandardCharsets.UTF_8)
        );

        if (!validKey) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Invalid bootstrap key"
            );
        }

        return authService.createFirstAdmin(request);
    }
}
