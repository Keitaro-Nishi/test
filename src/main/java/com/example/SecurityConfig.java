package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import com.example.User;
import com.example.userService;

//@Controller
//@Configuration
@SpringBootApplication
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Autowired
	private userService userservice;

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	private static final String USER_QUERY = "SELECT custid, password,role FROM userdata WHERE custid = ?";
	private static final String ROLE_QUERY = "SELECT custid, reserve FROM userdata WHERE custid = ?";

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
        //.passwordEncoder(passwordEncoder())
		.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery(USER_QUERY)
		.authoritiesByUsernameQuery(ROLE_QUERY);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/login").permitAll()
		.antMatchers("/**").hasAnyAuthority("ADMIN","USER")
		.antMatchers("/Home").hasAnyAuthority("ADMIN","USER")
		.antMatchers("/Account").hasAuthority("ADMIN")
		.antMatchers("/Account/**").hasAuthority("ADMIN")
		.antMatchers("/logout").hasAnyAuthority("ADMIN","USER")
		//.antMatchers("/User/**").hasAuthority("ADMIN")
		.and()
		.formLogin()
		.loginPage("/login");
		http.formLogin()
		.defaultSuccessUrl("/Home", true)
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/")
		.deleteCookies("JSESSIONID")
		.invalidateHttpSession(true).permitAll()
		.and()
		.csrf()
		.disable();
	}

	@Bean
	public DataSource dataSource() throws SQLException {
		if (dbUrl == null || dbUrl.isEmpty()) {
			return new HikariDataSource();
		} else {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(dbUrl);
			return new HikariDataSource(config);
		}
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}


