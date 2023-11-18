package com.example.peernow360.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//스프링이 돌아가는데 있어서 설정을 관리하는 객체
@Configuration
public class CorsFilterConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); //
        config.addAllowedHeader("*"); //클라이언트가 전송할 수 있는 헤더 값을 설정
        config.addAllowedMethod("*");
        config.addExposedHeader("*");

        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config); // CORS 구성을 등록, “/**” 는 모든 경로에서 CORS가 적용되도록 설정한다.
        return new CorsFilter(source);

    }

}
