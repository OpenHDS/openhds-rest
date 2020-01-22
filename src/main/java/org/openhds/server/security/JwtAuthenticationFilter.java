package org.openhds.server.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.openhds.server.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authManager;
    private SecurityProperties properties;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, final SecurityProperties securityProperties){
        this.authManager = authenticationManager;
        this.properties = securityProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try{
            User usr = new ObjectMapper().readValue(req.getInputStream(), User.class);
            return authManager.authenticate(new UsernamePasswordAuthenticationToken(usr.getUsername(),
                    usr.getPassword(), new ArrayList<>()));
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth){
        System.out.println("Authentication: " + ((UserDetails) auth.getPrincipal()).getUsername());
        System.out.println(properties.getExpirationTime());
        String token = JWT.create()
                .withSubject(((UserDetails) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + properties.getExpirationTime()))
                .sign(Algorithm.HMAC512(properties.getSecret().getBytes()));

        res.addHeader(properties.getHeaderString(), properties.getTokenPrefix() + " " + token);
    }
}