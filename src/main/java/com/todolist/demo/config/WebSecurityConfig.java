package com.todolist.demo.config;

import com.todolist.demo.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors() // 기본 cors
            .and()
            .csrf() // csrf 미사용
                .disable()
            .httpBasic() // httpBasic 미사용 (=token 사용)
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests() // 인증 제외할 경로 등록
                .antMatchers("/", "/auth/**").permitAll()
            .anyRequest() // 그 외 모든 경로는 인증 거침
                .authenticated();
        // 매 리퀘스트마다 CorsFilter 실행한 후에 jwtAuthenticationFilter 실행하는 Filter 등록
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
    }
}
