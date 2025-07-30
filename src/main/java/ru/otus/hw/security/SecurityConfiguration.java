package ru.otus.hw.security;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@EnableCaching
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))

                .authorizeHttpRequests(authorize -> {
                    authorize
                            //.requestMatchers(HttpMethod.POST,"/api/**").not().fullyAuthenticated()
                            .requestMatchers("/roles/**").hasRole("ADMIN")
                            .requestMatchers("/users/**").hasRole("ADMIN")
                            .requestMatchers("/api/roles/**").hasRole("ADMIN")
                            .requestMatchers("/api/users/**").hasRole("ADMIN")
                            .requestMatchers("/api/webhooktest/**").permitAll()
                            .anyRequest().authenticated();
                })
                //.httpBasic(Customizer.withDefaults())
                //.oauth2Client(withDefaults())
                .formLogin(Customizer.withDefaults())
                .rememberMe(rm -> rm.key("key").tokenValiditySeconds(600))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);// 12- это соль
    }


}
