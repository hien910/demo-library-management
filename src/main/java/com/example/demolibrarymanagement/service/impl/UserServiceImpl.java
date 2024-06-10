package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.DTO.request.LoginRequest;
import com.example.demolibrarymanagement.DTO.request.RegisterRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertUser;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Role;
import com.example.demolibrarymanagement.model.entity.Token;
import com.example.demolibrarymanagement.model.entity.User;
import com.example.demolibrarymanagement.repository.RoleRepository;
import com.example.demolibrarymanagement.repository.UserRepository;
import com.example.demolibrarymanagement.security.JwtUtil;
import com.example.demolibrarymanagement.security.SecurityConfig;
import com.example.demolibrarymanagement.service.ITokenService;
import com.example.demolibrarymanagement.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;


import javax.security.auth.login.AccountLockedException;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final ITokenService tokenService;

    @Override
    public String login(LoginRequest loginRequest, HttpServletRequest request) throws DataNotFoundException, AccountLockedException {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException("Not found user with this email:" + loginRequest.getEmail()));
        // Tạo đối tượng xác thực
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        try {
            // Tiến hành xác thực
            Authentication authentication = authenticationManager.authenticate(token);
            // Lưu đối tượng đã xác thực vào trong SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Create JWT token and return
            return jwtUtil.generateToken(user);
        } catch (DisabledException e) {
            throw new RuntimeException("Tài khoản chưa được xác thực");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }
    }

    @Override
    public User register(RegisterRequest request) throws DataNotFoundException {
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new DataNotFoundException("Mật khẩu và xác nhận mật khẩu không trùng khớp!");
        }
        Optional<User> userRequest = userRepository.findByEmail(request.getEmail());
        if(userRequest.isPresent()){
            throw new DataNotFoundException("Email đã được sử dụng!");
        }
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        Role role = roleRepository.findByName("USER");
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UpsertUser upsertUser, Integer id) throws DataNotFoundException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User loginUSer = (User) authentication.getPrincipal();
//        if(!loginUSer.getId().equals(id)){
//            throw new AccessDeniedException("Access denied: You are not authorized to access this resource.");
//        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + id));
        user.setName(upsertUser.getName());
        user.setEmail(upsertUser.getEmail());
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }
}
