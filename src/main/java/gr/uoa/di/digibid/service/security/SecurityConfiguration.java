package gr.uoa.di.digibid.service.security;

import gr.uoa.di.digibid.persist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DigiBidAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/user/register/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/items/**").permitAll()  //TODO improve
                .antMatchers("/item/**").permitAll()  //TODO improve
                .antMatchers("/help").permitAll()  //TODO improve
                .antMatchers("/search").permitAll()  //TODO improve
                .antMatchers("/api/items/download/**").hasAuthority(User.Role.ADMIN.toString())
                .antMatchers("/api/items/upload/**").hasAuthority(User.Role.ADMIN.toString())
                .antMatchers("/users/**").hasAuthority(User.Role.ADMIN.toString())
                .antMatchers("/items/users/**").hasAnyRole(User.Role.BIDDER.toString(), User.Role.SELLER.toString())
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler)
                .failureUrl("/login?error")
                .usernameParameter("username")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("remember-me")
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .rememberMe();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}