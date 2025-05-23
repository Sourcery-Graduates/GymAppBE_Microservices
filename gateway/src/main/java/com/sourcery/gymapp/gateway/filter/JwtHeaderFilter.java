package com.sourcery.gymapp.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
//public class JwtHeaderFilter implements GatewayFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        Authentication authentication = ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication)
//                .block(); // NIE w produkcji — użyj Reactive stylu, to dla uproszczenia
//
//        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
//            Jwt jwt = jwtAuth.getToken();
//            String userId = jwt.getClaimAsString("sub");
//            exchange.getRequest()
//                    .mutate()
//                    .header("X-User-Id", userId)
//                    .build();
//        }
//
//        return chain.filter(exchange);
//    }
//}
