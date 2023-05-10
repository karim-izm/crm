package com.example.application.repository;

import com.example.application.models.User;
import com.example.application.models.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VenteRepository extends JpaRepository<Vente , Integer> {
    @Query("SELECT v FROM Vente v WHERE v.date BETWEEN :startDate AND :endDate AND v.agent = :agent")
    List<Vente> findByDateVenteBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate , @Param("agent") User agent);

    @Query("SELECT v FROM Vente v WHERE v.date BETWEEN :startDate AND :endDate")
    List<Vente> findByDateVenteBetweenAll(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT v FROM Vente v WHERE v.agent = :agent")
    List<Vente> allVentes(@Param("agent") User agent);
    Optional<Vente> findById(int id);


    @Query("SELECT COUNT(v) FROM Vente v WHERE v.agent = :agent")
    Long countByAgent(@Param("agent") User agent);
}
