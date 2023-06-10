package com.example.application.repository;

import com.example.application.models.User;
import com.example.application.models.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.YearMonth;
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

    @Query("SELECT COUNT(v) FROM Vente v WHERE v.qualification = 'Accepté'")
    long countAcceptedVentes();

    @Query("SELECT COUNT(v) FROM Vente v WHERE v.qualification = 'KO'")
    long countKOVentes();

    @Query("SELECT COUNT(v) FROM Vente v WHERE v.qualification = 'En Attente'")
    long countEnAttentVentes();

    @Query("SELECT COUNT(v) FROM Vente v WHERE v.qualification = 'Effectif'")
    long countEffectifVentes();

    @Query("SELECT COUNT(v) FROM Vente v WHERE v.qualification = 'Pas Encore Qualifié'")
    long countPasEncoreentes();
    @Query("SELECT COUNT(v) FROM Vente v WHERE v.agent = :agent")
    Long countByAgent(@Param("agent") User agent);

    @Query("SELECT COUNT(v) FROM Vente v WHERE v.agent = :user AND MONTH(v.date) = :month AND YEAR(v.date) = :year")
    Integer getTotalSalesForUserAndMonth(User user, int month, int year);

    @Query("SELECT MONTH(v.date), COUNT(v) FROM Vente v GROUP BY MONTH(v.date)")
    List<Object[]> countVentesByMonth();

    @Query("SELECT v.typeVente, COUNT(v) FROM Vente v GROUP BY v.typeVente")
    List<Object[]> countVenteTypes();
}
