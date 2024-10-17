package com.sparta.springtrello.domain.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sparta.springtrello.domain.user.config.JwtUtil;
import com.sparta.springtrello.domain.user.entity.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserArgumentResolver.class);

    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return AuthUser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = jwtUtil.getTokenFromRequest(request);

        logger.info("Generated JWT Token: {}", token);

        // Trim the token to remove any leading or trailing whitespace
        if (token != null) {
            token = token.trim(); // Trim any spaces

            if (jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.getUserInfoFromToken(token);
                Long userId = Long.valueOf(claims.getSubject());
                String email = claims.get("email", String.class);
                Role role = Role.valueOf(claims.get("role", String.class));
                log.info("user_id ::: {}, role ::: {} ", userId, role);

                return new AuthUser(email, role, userId);
            }
        }

        return null;
    }

}
