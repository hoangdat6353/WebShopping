package com.project.config;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.service.UserDetailsServiceImpl;
 
@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
    	http.authorizeRequests().antMatchers("/login", "/logout").permitAll();
    	
    
        http.authorizeRequests().antMatchers("/accountProfile").access("hasAnyRole('ROLE_USER', 'ROLE_DEV')");
        http.authorizeRequests().antMatchers("/upgrade").access("hasRole('ROLE_USER')");
        http.authorizeRequests().antMatchers("/devProfile","/index-dev","/dev-apps","/approval").access("hasRole('ROLE_DEV')");

        // Trang chỉ dành cho ADMIN
        http.authorizeRequests().antMatchers("/index-admin","/verify-apps","/accounts","/categories","/cards").access("hasRole('ROLE_ADMIN')");
    	
    	http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
    	
        http.authorizeRequests().and().formLogin()
        	.loginPage("/login")
        	.defaultSuccessUrl("/index",true)
        	.loginProcessingUrl("/loginHandler")
        	.failureUrl("/login?error=true")
        	.usernameParameter("username")
        	.passwordParameter("password")
        	.and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");
            
    }
}