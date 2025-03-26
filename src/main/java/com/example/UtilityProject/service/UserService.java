package com.example.UtilityProject.service;

import com.example.UtilityProject.model.User;
import com.example.UtilityProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getUniqueUsers() {
        return userRepository.findDistinctUsers();
    }
}

