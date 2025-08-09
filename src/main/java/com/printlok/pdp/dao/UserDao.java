package com.printlok.pdp.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.printlok.pdp.models.user.User;
import com.printlok.pdp.repositories.user.UserRepository;

@Repository
public class UserDao {
	@Autowired
	private UserRepository userRepository;

	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	 public boolean existsByEmail(String email) {
	        return userRepository.existsByEmail(email);
	    }

	    public boolean existsByPhone(String phone) {
	        return userRepository.existsByPhone(phone);
	    }

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
