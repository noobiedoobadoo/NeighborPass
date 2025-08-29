package com.pranay.apigatewayff.config;

import com.pranay.apigatewayff.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {  // Change to GlobalFilter for all routes

    private static final AntPathMatcher matcher = new AntPathMatcher();

    private final JwtService jwtService;  // Autowire it

    public JwtAuthenticationFilter(JwtService jwtService) {  // Constructor injection
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        System.out.println("Request Path: " + path);  // This should now print for every request

        // Skip authentication for login/register endpoints
        if (path.startsWith("/auth")) {
            System.out.println("IN AUTH");
            return chain.filter(exchange);
        }

        // Check for Authorization header
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtService.isTokenValid(token)) {
                return unauthorized(exchange);
            }

            // Extract user info and add to request headers
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            // Enforce simple path-based role rules
            if (path.startsWith("/admin") && !"ROLE_ADMIN".equals(role)) {
                return forbidden(exchange);
            }

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Name", username)
                    .header("X-Authority", role)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            return unauthorized(exchange);
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange ex) {
        ex.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return ex.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange ex) {
        ex.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return ex.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;  // Run early in the filter chain
    }
}
/*

package com.pranay.apigatewayff.config;

import com.pranay.apigatewayff.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    private static final AntPathMatcher matcher = new AntPathMatcher();

    private final JwtService jwtService = new JwtService();



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Skip authentication for login/register endpoints
        String path = request.getURI().getPath();
        System.out.println(path);
        if (path.startsWith("/auth")) {
            System.out.println("IN AUTH");
            return chain.filter(exchange);
        }

        // Check for Authorization header
        String authHeader = request.getHeaders().getFirst("Authorization");
        List<String> authHeaders = exchange.getRequest().getHeaders().get("Authorization");


        if (authHeaders == null || authHeader.isEmpty()) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtService.isTokenValid(token)) {
                return unauthorized(exchange);
            }

            // Extract user info and add to request headers
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Name", username)
                    .build();

            // 4. **Enforce simple path-based role rules**
            if (path.startsWith("/admin") && !role.equals("ROLE_ADMIN")) {
                return forbidden(exchange);
            }

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            return unauthorized(exchange);
        }
    }

    private boolean isAdminPath(String path) {
        return matcher.match("/admin/**", path);
    }

    private Mono<Void> unauthorized(ServerWebExchange ex) {
        ex.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return ex.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange ex) {
        ex.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return ex.getResponse().setComplete();
    }

}

 */
