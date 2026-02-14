package com.training.some_social_network.clients;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.training.some_social_network.security.RequestIdFilter.REQUEST_ID_HEADER;
import static com.training.some_social_network.security.RequestIdFilter.REQUEST_ID_KEY;
import static com.training.some_social_network.security.jwt.JwtTokenProvider.AUTHORIZATION_HEADER;

@Configuration
public class ClientRequestInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader(AUTHORIZATION_HEADER);
                if (token != null) {
                    requestTemplate.header(AUTHORIZATION_HEADER, token);
                }
                String requestId = MDC.get(REQUEST_ID_KEY);
                if (requestId != null) {
                    requestTemplate.header(REQUEST_ID_HEADER, requestId);
                }
            }
        };
    }
}