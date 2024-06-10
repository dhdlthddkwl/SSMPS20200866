package capstone_design_1.ssmps_backend.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
            log.error("token: {}", token);
            if(token != null && jwtTokenProvider.validateToken(token)){
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (ExpiredJwtException e){
            log.error("token expired: {}", e.getMessage());
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
        }catch (JwtException e){
            log.error("Invalid JWT token");
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
        }
        chain.doFilter(request, response);
    }
}