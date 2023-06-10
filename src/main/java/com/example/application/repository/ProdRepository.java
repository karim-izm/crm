package com.example.application.repository;

import com.example.application.models.User;
import com.example.application.models.Vente;
import com.example.application.models.WorkTime;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProdRepository extends JpaRepository<WorkTime , Integer> {
    @Query("SELECT COUNT(w) FROM WorkTime w WHERE w.inProd = true")
    long countUsersInProd();

    List<WorkTime> findByAgent(User user);

    @Query("SELECT wt FROM WorkTime wt WHERE wt.startTime >= :startDate AND wt.startTime <= :endDate")
    List<WorkTime> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
