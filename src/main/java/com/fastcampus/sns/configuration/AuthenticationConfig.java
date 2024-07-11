package com.fastcampus.sns.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception { // http 관련 설정
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/api/*/users/join", "/api/*/users/login", "/h2-console/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//               TODO exception 발생 시 특정 엔트리포인트로 가도록 설정
//                .exceptionHandling()
//                .authenticationEntryPoint()
    }
}
