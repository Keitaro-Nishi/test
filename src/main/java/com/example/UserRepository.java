package com.example;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

	public interface UserRepository extends JpaRepository<User, String>{
	}


