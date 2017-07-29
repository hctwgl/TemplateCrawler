package com.seveniu.security.auth.jwt.config;

import com.seveniu.security.auth.jwt.jwt.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    static final String LOGIN_URL = "/login";
    static final String AUTH_URL = "/auth/**";
    private final UserDetailsService userServiceImpl;
    private final HttpExceptionEntryPoint httpExceptionEntryPoint;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public WebSecurityConfig(UserDetailsService userServiceImpl, HttpExceptionEntryPoint httpExceptionEntryPoint, AuthenticationFailureHandler authenticationFailureHandler, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.userServiceImpl = userServiceImpl;
        this.httpExceptionEntryPoint = httpExceptionEntryPoint;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()//Disabled CSRF protection
                .disable();

        // don't create session
        http

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(httpExceptionEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_URL).permitAll()
                .antMatchers(AUTH_URL).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/auth/**").permitAll()//allow CORS option calls
                .antMatchers("/api/**").authenticated()//allow CORS option calls
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/login")
                .failureHandler(authenticationFailureHandler)
                .successHandler(authenticationSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl(LOGIN_URL)
                .permitAll()
        ;

        // Custom JWT based security filter
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        http.headers().cacheControl();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userServiceImpl)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userServiceImpl);
//    }

//    @Configuration
//    public class CORSConfiguration {
//
//        @Bean
//        public FilterRegistrationBean corsFilter() {
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowCredentials(true);
//            // 设置你要允许的网站域名，如果全允许则设为 *
//            config.addAllowedOrigin("*");
//            // 如果要限制 HEADER 或 METHOD 请自行更改
//            config.addAllowedHeader("*");
//            config.addAllowedMethod("*");
//            source.registerCorsConfiguration("/**", config);
//            FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//            // 这个顺序很重要哦，为避免麻烦请设置在最前
//            bean.setOrder(0);
//            return bean;
//        }
//    }
}
