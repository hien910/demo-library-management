package com.example.demolibrarymanagement.security;

import com.example.demolibrarymanagement.model.entity.Permission;
import com.example.demolibrarymanagement.model.entity.Role;
import com.example.demolibrarymanagement.model.entity.User;
import com.example.demolibrarymanagement.repository.RoleRepository;
import com.example.demolibrarymanagement.service.IPermissionService;
import com.example.demolibrarymanagement.service.IRoleService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Order(value = 3)
@RequiredArgsConstructor
public class DynamicAuthorityFilter extends OncePerRequestFilter {
    private final RoleRepository roleRepository;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            filterChain.doFilter(request, response);
            return;
        }
        User user = (User) authentication.getPrincipal();
        if(user.getRole().getName().equals("ADMIN")){
            filterChain.doFilter(request, response);
            return;
        }
        String requestPath = request.getRequestURI();
        Role role = roleRepository.findRoleById(user.getRole().getId());
        List<Permission> permissionList = role.getPermissions();

        boolean isValid=false;
        for(Permission permission : permissionList){
            if (requestPath.equals(permission.getUrl())) {
                isValid=true;
                break;
            }
        }
        if(isValid){
            filterChain.doFilter(request, response);
            return;
        }
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allow");
        throw new ServletException("Method Not Allow");
    }
}