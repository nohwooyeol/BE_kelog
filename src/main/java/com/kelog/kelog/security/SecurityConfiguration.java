package com.kelog.kelog.security;


import com.kelog.kelog.security.jwt.AccessDeniedHandlerException;
import com.kelog.kelog.security.jwt.AuthenticationEntryPointException;
import com.kelog.kelog.security.jwt.JwtConfiguration;
import com.kelog.kelog.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

    @Value("${jwt.secret}")
    private String secretKey;
    @Autowired
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    private final AccessDeniedHandlerException accessDeniedHandlerException;

    private final AuthenticationEntryPointException authenticationEntryPointException;


    // ------------------------뭔지 모름 검색 해봐야함
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**");
    }
    // --------------- 비밀번호 암호화 빈 생성 -----------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//     ----------------시큐리티 필터 선언 -----------------------
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        cors 사용하겠다!
        http.cors();

//        csrf 비활성화!!
        http.csrf().disable()

//                예외 처리
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException)
                .accessDeniedHandler(accessDeniedHandlerException)

//                세션 미사용 설정! 토큰은 세션이 필요가 없응께!
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//                api 허용 목록!
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/**").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/register").permitAll()
                .anyRequest().authenticated()

//                필터 적용
                .and()
                .apply(new JwtConfiguration( secretKey, tokenProvider, userDetailsService));

        return http.build();
    }

}
