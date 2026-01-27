package com.vikash.journalApp.srvices;

import com.vikash.journalApp.entity.User;
import com.vikash.journalApp.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user) {
        userRepository.save(user);
    }
    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }


    public User findUserByName(String userName) {
        return userRepository.findByUsername(userName).orElse(null);
    }


    public User updateEntry(String userName, User user) {
        Optional<User> existingEntryOpt = userRepository.findByUsername(userName);

        if (existingEntryOpt.isPresent()) {
            User existingEntry = existingEntryOpt.get();

            if (!user.getPassword().equals(existingEntry.getPassword())) {
                existingEntry.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            return userRepository.save(existingEntry);
        }

        return null;
    }

    public void deleteByUserName(String userName) {
        userRepository.deleteByUsername(userName);
    }


    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(ObjectId userID) {
        return userRepository.findById(userID);
    }

    public void deleteUserByID(ObjectId userId) {
        userRepository.deleteById(userId);
    }
}
