package com.example.peernow360.security;


//import com.btc.jwtlogin.exception.JwtExceptionFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@Configuration
@Log4j2
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final JWTtokenFilter jwTtokenFilter;
//    private final JwtExceptionFilter jwtExceptionFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info("filterChain");

        http.csrf().disable()
                .cors().disable()
                .httpBasic().disable()  // HTTP 기본 인증 방식을 비활성화
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()  // HTTP 요청 인증 설정
                        .requestMatchers("/join","/login", "/request_refreshToken").permitAll()
                        .anyRequest().authenticated()  // 해당 경로 외의 요청은 모두 인증 필요
                ).formLogin().disable()
                // 여기서부터 로그아웃 API 내용~
//                .logout()
//                .logoutUrl("/logout_info") // 로그아웃 처리 URL (= form action url)
//                .logoutSuccessUrl("/")
//                .permitAll() // 모든 사용자에게 로그아웃을 허용
//                .deleteCookies("refreshToken")
//                .clearAuthentication(true)
//                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))

                // X-Frame-Options 헤더 설정을 SAMEORIGIN으로 설정하여, 웹 페이지를 iframe으로 삽입하는 공격 방지를 위한 설정
//                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()



                // 세션을 사용하지 않기 때문에 STATELES로 설정
//                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // exception handling
                .and()
                .exceptionHandling() //예외 처리를 설정
                .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출할 엔드포인트를 설정
                .accessDeniedHandler(customAccessDeniedHandler) //인가되지 않은 사용자가 보호된 리소스에 접근할 때 호출할 핸들러를 설정

                // jwt filter -> 인증 정보 필터링 전에(filterBefore) 필터
                // 지정된 필터 앞에 커스텀 필터를 추가 (UsernamePasswordAuthenticationFilter 보다 먼저 실행된다)
                .and()
                .addFilterBefore(jwTtokenFilter, UsernamePasswordAuthenticationFilter.class);
//                .addFilterBefore(jwtExceptionFilter, JWTtokenFilter.class);

        return http.build();

    }

}
