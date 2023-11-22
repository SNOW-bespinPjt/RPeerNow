package com.example.peernow360.security;


//import com.btc.jwtlogin.exception.JwtExceptionFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .authorizeHttpRequests(authorize-> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()  // HTTP 요청 인증 설정
                        .requestMatchers("/api/user/join","/api/user/login","/api/user/request_refreshToken","/api/user/gettest", "/v3/**", "/swagger-ui/**"
                        ).permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/api/project/sprint").hasAnyRole("PM", "SM")
//                        .requestMatchers("/api/project/sprint").hasRole("SM")
//                        .requestMatchers(HttpMethod.PUT,"/api/project").hasRole("PM")
//                        .requestMatchers(HttpMethod.DELETE,"/api/project").hasRole("PM")
                        .anyRequest().authenticated()  // 해당 경로 외의 요청은 모두 인증 필요
                ).formLogin().disable()



                .headers()
                .frameOptions()
                .disable()
//                .sameOrigin()
                .and()
                .addFilter(corsFilter)

                // 세션을 사용하지 않기 때문에 STATELES로 설정
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

        return http.build();

    }

}
