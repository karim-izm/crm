package com.example.application.service;

import com.example.application.models.User;
import com.example.application.repository.UserRepository;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository ){
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers(String text){
        if(text.isEmpty() || text.isBlank() || text == null){
            return userRepository.findAll();
        }
        else{
            return userRepository.search(text);
        }
    }

    public int login(String username , String password){
        User user = userRepository.login(username, password);
        if(user != null){
            VaadinSession.getCurrent().setAttribute("user", user);
            if(user.isAdmin()){
                return 1;
            }
            else return 0;
        }
        return -1;
    }

    public void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
    }

    public long countUsers(){
        return userRepository.count();
    }

    public void addUser(User u ){
        if(u == null){
            System.err.println("User is NULL");
            return;
        }
            u.setAdmin(false);
            userRepository.save(u);
    }
    public void deleteUser(User u ){
        userRepository.delete(u);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean changeUserPassword(User user, String oldPassword, String newPassword) {
        if (oldPassword.equals(user.getPassword())) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }



}
