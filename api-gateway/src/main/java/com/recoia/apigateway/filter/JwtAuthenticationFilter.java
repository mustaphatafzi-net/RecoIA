package com.recoia.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLE = "X-User-Role";

    private final SecretKey key;

    // jwt.secret vient du config-server : c'est la MEME cle que celle d'auth-service
    public JwtAuthenticationFilter(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //http://localhost:8080/api/catalog/courses
        ServerHttpRequest original = exchange.getRequest(); //la requête complète, en lecture seule
        String path = original.getURI().getPath(); //"/api/catalog/courses"
        HttpMethod method = original.getMethod(); //Get exemple

        // 0. Securite : on supprime X-User-Id / X-User-Role envoyes par le client.
        //    Seule la gateway a le droit de les remplir.
        ServerHttpRequest clean = original.mutate() //copie  //clean requete sans id et role
                .headers(h -> { //acces au en tete
                    h.remove(HEADER_USER_ID);
                    h.remove(HEADER_USER_ROLE);
                })
                .build();

        // 1. Routes publiques : pas besoin de token
        if (isPublic(path, method)) {
            return chain.filter(exchange.mutate().request(clean).build());
        }

        // 2. Il faut un token "Authorization: Bearer xxxxx"
        String authorization = original.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return reject(exchange, HttpStatus.UNAUTHORIZED, "Token manquant");
        }
        String token = authorization.substring(7);

        // 3. Verifier la signature et la date d'expiration
        Claims claims;
        try {
            claims = Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return reject(exchange, HttpStatus.UNAUTHORIZED, "Token invalide ou expire");
        }

        String userId = claims.getSubject(); //1
        String role = claims.get("role", String.class); //ADMIN

        // 4. Routes reservees aux ADMIN
        if (requiresAdmin(path, method) && !"ADMIN".equals(role)) {
            return reject(exchange, HttpStatus.FORBIDDEN, "Acces reserve aux administrateurs");
        }

        // 5. Token valide : on transmet avec l'identite de l'utilisateur
        ServerHttpRequest authenticated = clean.mutate()
                .header(HEADER_USER_ID, userId)
                .header(HEADER_USER_ROLE, role)
                .build();
        return chain.filter(exchange.mutate().request(authenticated).build());
    }

    private boolean isPublic(String path, HttpMethod method) {
        if (HttpMethod.OPTIONS.equals(method)) {
            return true; // requetes de pre-verification du navigateur (CORS)
        }
        return HttpMethod.POST.equals(method)
                && (path.equals("/api/auth/login") || path.equals("/api/auth/register"));
    }

    private boolean requiresAdmin(String path, HttpMethod method) {
        if (path.startsWith("/api/auth/admins")) {
            return true;
        }
        // Catalogue : lecture (GET) pour tous, ecriture reservee aux ADMIN
        // (la recherche GraphQL, en POST, sera autorisee plus tard)
        return path.startsWith("/api/catalog/")
                && !HttpMethod.GET.equals(method)
                && !path.equals("/api/catalog/graphql");
    }

    private Mono<Void> reject(ServerWebExchange exchange, HttpStatus status, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] body = ("{\"error\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(body);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1; // avant les autres filtres, donc avant StripPrefix
    }
}
