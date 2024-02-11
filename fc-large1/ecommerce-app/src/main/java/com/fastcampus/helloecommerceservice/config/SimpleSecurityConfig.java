package com.fastcampus.helloecommerceservice.config;

import com.fastcampus.helloecommerceservice.domain.customer.CustomerDetailService;
import com.fastcampus.helloecommerceservice.enums.ECommerceRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@EnableWebSecurity
public class SimpleSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomerDetailService customerDetailService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors().disable();

        httpSecurity
            .formLogin()
            .loginPage("/customer/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
            .failureUrl("/customer/login?error=true")
            .and()
            .logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
            .authorizeHttpRequests(authorize -> authorize
                    .antMatchers("/cart/**").hasRole(String.valueOf(ECommerceRole.CUSTOMER))
                .antMatchers("/checkout/**").hasRole(String.valueOf(ECommerceRole.CUSTOMER))
                .antMatchers("/customer/my-page*").hasRole(String.valueOf(ECommerceRole.CUSTOMER))
                .antMatchers("/**").permitAll());

        httpSecurity.exceptionHandling().authenticationEntryPoint(new SimpleAuthenticationEntryPoint());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerDetailService).passwordEncoder(passwordEncoder());
    }
}

