package com.example.application.repository;

import com.example.application.models.User;
import com.example.application.models.WorkTime;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProdRepository extends JpaRepository<WorkTime , Integer> {

}
