package com.kelog.kelog.security.jwt;

import com.kelog.kelog.domain.Member;

import com.kelog.kelog.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;


@Slf4j
@Component
public class TokenProvider {

    // secretKey 와 같은 민감정보는 숨기는 것이 좋다. (이것은 연습이라서 노출함)


    // 토큰 유효시간 5분 설정 (1000L = 1초, 1000L * 60 = 1분)
    //    엑세스 토큰만 있어 10분으로 설정
    private static final long TOKEN_VALID_TIME = 1000L * 60 * 60;
    private final UserDetailsServiceImpl userDetailsService;
    public static String BEARER_PREFIX = "Bearer ";
    private final Key key;
    private final String secretKey;
    //    value로 1시간 고민하다 은진매니저님의 것을 보고 발견한 것
    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         UserDetailsServiceImpl userDetailsService) {
        this.secretKey = secretKey;
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    //---------------------------------------------------------------------------------------------------

    // JWT 토큰 생성
    public String createToken(Member member) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(member.getAccount());
        String Token = Jwts.builder()
                .setSubject(member.getAccount()) // 유저 정보 저장
                .setClaims(claims)  // 권한 정보 저장
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME)) // 만료 시간 정보
                .signWith(key, SignatureAlgorithm.HS256) // 키값과 알고리즘 세팅
                .compact();
        return BEARER_PREFIX+Token;
    }

    //---------------------------------------------------------------------------------------------------
    // 토큰 유효성 확인
//    parserbuilder가 어떤 역할인지 알아내야겠음;;; 어려웡
    public boolean CheckToken(HttpServletRequest request) {
        String jwtToken = takeToken(request);
        try {
            Jwts
                    .parserBuilder()
//                    jwt 서명 검증을 위한 secret key를 들고온다.
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }
    //---------------------------------------------------------------------------------------------------
    // JWT 토큰에서 인증 정보 조회 (Account 조회)
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //---------------------------------------------------------------------------------------------------
    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserAccount(HttpServletRequest request) {
        String jwtToken = takeToken(request);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody().getSubject();
    }
    //---------------------------------------------------------------------------------------------------

    // 리퀘스트 헤더에서 토큰값가져오기
    public String takeToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }


}