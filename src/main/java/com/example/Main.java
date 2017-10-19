package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.encoding.BasePasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import org.springframework.validation.BindingResult;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

	@Autowired
	   userService userservice;

	@GetMapping("/signup")
	    public String signup(Model model) {
	        model.addAttribute("signupform", new signupForm());
	        return "signup";
	    }

	@PostMapping("/signup")
	    public String signupPost(Model model, @Valid signupForm signupform, BindingResult bindingResult, HttpServletRequest request) {
	        if (bindingResult.hasErrors()) {
	            return "signup";
	        }

	        try {
	            userservice.registerUser(signupform.getCustid(),signupform.getUsername(), signupform.getOrgname(),signupform.getPassword());
	        }catch (DataIntegrityViolationException e) {
	            //model.addAttribute("signupError", true);
	        	//e.printStackTrace();
	            return "signup";
	         }

	        return "signup";
	    }


	@RequestMapping({"/","/login"})
	String login() {
		return "login";
	}

	@RequestMapping("/Home")
	String Home() {
		return "Home";
	}

	@RequestMapping("/signup")
	String signup() {
		return "signup";
	}

	@RequestMapping("/Account")
	String Account() {
		return "Account";
	}

	@RequestMapping("/index")
	String index() {
		return "index";
	}

	@RequestMapping("/logview")
	String logview() {
		return "logview";
	}
}