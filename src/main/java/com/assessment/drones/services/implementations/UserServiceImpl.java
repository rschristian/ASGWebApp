package com.assessment.drones.services.implementations;

import com.assessment.drones.domain.User;
import com.assessment.drones.domain.VerificationToken;
import com.assessment.drones.repository.interfaces.UserRepository;
import com.assessment.drones.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean emailAlreadyInUse(String email) {
        return userRepository.findUserByEmail(email) == null;
    }

    @Override
    public void createVerificationToken(VerificationToken verificationToken){
        userRepository.createVerificationToken(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token){
        return userRepository.getVerificationToken(token);
    }

    @Override
    public void authenticateUser(String userEmail) {
        userRepository.authenticateUser(userEmail);
    }

}