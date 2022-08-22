package com.kelog.kelog.security;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration // 설정파일 선언
@EnableWebSecurity //시큐리티 활성화
@EnableGlobalMethodSecurity(securedEnabled = true) 
//메소드시큐리티를 사용가능하게 선언한다. (데스크탑시큐리티를 위한 기능) Role 권한 등
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
// 웹 어플리케이션 컨텍스트일 경우 활성화 한다.
public class SecurityConfiguration {

    @Value("${jwt.secret}")
    private String secretKey;
    @Autowired
    private final com.kelog.kelog.security.jwt.TokenProvider tokenProvider;
    private final com.kelog.kelog.security.UserDetailsServiceImpl userDetailsService;

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

//                세션 미사용 설정! 토큰은 세션이 필요가 없응께!
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//                api 허용 목록!
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()

//                필터 적용
                .and()
                .apply(new com.kelog.kelog.security.jwt.JwtConfiguration( secretKey, tokenProvider, userDetailsService));

        return http.build();
    }

}
