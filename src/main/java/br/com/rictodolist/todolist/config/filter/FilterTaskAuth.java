package br.com.rictodolist.todolist.config.filter;

import br.com.rictodolist.todolist.repositories.IUserRepository;
import br.com.rictodolist.todolist.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = this.recoverToken(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = this.jwtService.validateToken(jwt);

        UserDetails user = this.userRepository.findByUsername(username).get();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        final String jwtType = "Bearer ";
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(jwtType)) {
            return null;
        }

        return authHeader.substring(jwtType.length());
    }
}
