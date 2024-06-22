package com.vinn.vinnet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.vinn.vinnet.security.OurUserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OurUserDetailService userDetailService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/templates/**").permitAll()
                        .requestMatchers("/vinnet/register").permitAll()     
                        .requestMatchers("/api/user/create").permitAll()
                        // to test
                        .requestMatchers("/api/uploadImage").permitAll()
                        .requestMatchers("/api/friendship/addFriend").permitAll()
                        .requestMatchers("/api/friendship/getAllPendingFriends").permitAll()
                        .requestMatchers("/api/friendship/getAllFriends").permitAll()
                        .requestMatchers("/api/friendship/mutualFriends").permitAll()
                        .requestMatchers("/api/friendship/removeFriend").permitAll()
                        .requestMatchers("/api/friendship/acceptOrRemoveFriend").permitAll()
                        .requestMatchers("/api/getFileUrl").permitAll()
                        .requestMatchers("/api/post/view").permitAll()
                        .requestMatchers("/api/search").permitAll()
                        .requestMatchers("/api/post/react").permitAll()
                        // to test
                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        (exceptionHandling) -> exceptionHandling
                                .accessDeniedPage("/accessDenied")
                )
                .formLogin(form -> form
                                .loginPage("/vinnet/login")
                                .loginProcessingUrl("/signIn")
                                .defaultSuccessUrl("/vinnet/feed")
                                .successHandler(
                                        (((request, response, authentication) -> {
                                            response.sendRedirect("/vinnet/feed");
                                        }))
                                )
                                .failureHandler(authenticationFailureHandler())
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutUrl("/signOut")
                                .logoutSuccessUrl("/login")
                                .logoutSuccessHandler(
                                        (((request, response, authentication) -> {
                                            response.sendRedirect("/login");
                                        }))
                                )
                                .invalidateHttpSession(true)
                                .permitAll()
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new OurUserDetailService();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.sendRedirect("/login");
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String errorMessage = "Incorrect username or password.";
            request.getSession().setAttribute("errorMessage", errorMessage);
            response.sendRedirect("/login?error");
        };
    }
}
