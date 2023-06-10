package com.example.application.repository;

import com.example.application.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where lower(u.fullName) like lower(concat('%', :searchTerm, '%')) and u.isAdmin = false")
    List<User> search(@Param("searchTerm") String searchTerm);

    @Query("select u from User u where lower(u.username) = :username and lower(u.password) = :password")
    User login(@Param("username") String username , @Param("password") String password);

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.isAdmin = false")
    List<User> findAllNonAdminUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.enProd = true")
    Long getCountOfAgentsInProd();


}
