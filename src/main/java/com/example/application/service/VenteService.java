package com.example.application.service;

import com.example.application.models.User;
import com.example.application.models.Vente;
import com.example.application.repository.VenteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VenteService {
    private final VenteRepository venteRepository;

    public VenteService(VenteRepository venteRepository) {
        this.venteRepository = venteRepository;
    }

    public List<Vente> allVentes() {
        return venteRepository.findAll();
    }

    public void save(Vente v) {
        venteRepository.save(v);
    }

    public List<Vente> getFilteredVentes(String filterOption, User agent) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        LocalDate today = LocalDate.now();
        if (filterOption.equals("Tous")) {
            return venteRepository.allVentes(agent);
        } else if (filterOption.equals("15 derniers jours")) {
            startDate = today.minusDays(15);
            endDate = today;
        } else if (filterOption.equals("Ce mois")) {
            startDate = today.withDayOfMonth(1);
            endDate = today;
        } else if (filterOption.equals("Selectionner Mois")) {
            startDate = today.minusMonths(3).withDayOfMonth(1);
            endDate = today;
        }
        return venteRepository.findByDateVenteBetween(startDate, endDate, agent);
    }

    public List<Vente> getFilteredVentesAll(String filterOption) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        LocalDate today = LocalDate.now();
        if (filterOption.equals("Tous")) {
            return venteRepository.findAll();
        } else if (filterOption.equals("15 derniers jours")) {
            startDate = today.minusDays(15);
            endDate = today;
        } else if (filterOption.equals("Ce mois")) {
            startDate = today.withDayOfMonth(1);
            endDate = today;
        } else if (filterOption.equals("Selectionner Mois")) {
            startDate = today.minusMonths(3).withDayOfMonth(1);
            endDate = today;
        }
        return venteRepository.findByDateVenteBetweenAll(startDate, endDate);
    }

    public List<Vente> getVentesByMonth(YearMonth selectedMonth) {
        LocalDate startDate = selectedMonth.atDay(1);
        LocalDate endDate = selectedMonth.atEndOfMonth();
        return venteRepository.findByDateVenteBetweenAll(startDate, endDate);
    }

    // ...

    public List<Vente> getFilteredVentesForMonth(String filterOption, String monthNumber) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        LocalDate today = LocalDate.now();

        if (filterOption.equals("Selectionner Mois")) {
            // Get the current year
            int currentYear = Year.now().getValue();

            // Create a YearMonth object using the selected month and current year
            YearMonth selectedYearMonth = YearMonth.of(currentYear, Integer.parseInt(monthNumber));

            startDate = selectedYearMonth.atDay(1);
            endDate = selectedYearMonth.atEndOfMonth();
        } else {
            return getFilteredVentesAll(filterOption);
        }
        return venteRepository.findByDateVenteBetweenAll(startDate, endDate);
    }


    public Vente updateVente(Vente updatedVente) {
        Optional<Vente> existingVente = venteRepository.findById(updatedVente.getVenteId());

        if (existingVente.isPresent()) {
            Vente vente = existingVente.get();
            vente.setContract(updatedVente.getContract());
            vente.setDate(updatedVente.getDate());
            vente.setTypeVente(updatedVente.getTypeVente());
            vente.setNomClient(updatedVente.getNomClient());
            vente.setNumTel(updatedVente.getNumTel());
            vente.setEmailClient(updatedVente.getEmailClient());
            vente.setDateNaiss(updatedVente.getDateNaiss());
            vente.setQualification(updatedVente.getQualification());
            return venteRepository.save(vente);
        } else {
            throw new IllegalArgumentException("Vente not found with ID " + updatedVente.getVenteId());
        }
    }

    public long countVentes(){
        return venteRepository.count();
    }

    public long getAcceptedVentesCount() {
        return venteRepository.countAcceptedVentes();
    }

    public long countVentesByAgent(User agent){
        return venteRepository.countByAgent(agent);
    }

    public void deleteVente(Vente v){
        venteRepository.delete(v);
    }

    public int getTotalSalesForUserAndMonth(User user, int month , int year) {
        return venteRepository.getTotalSalesForUserAndMonth(user , month , year);
    }

    public List<Object[]> countVentesByMonth() {
        return venteRepository.countVentesByMonth();
    }
    
    public List<Object[]> countVentesByType(){
        return venteRepository.countVenteTypes();
    }

    public long getPasEncoreVenteStats() {
        return venteRepository.countPasEncoreentes();
    }

    public long getEffectifVentesCount() {
        return venteRepository.countEffectifVentes();
    }

    public long getEnAttentVentesCount() {
        return venteRepository.countEnAttentVentes();
    }

    public long getKoVentesCount() {
        return venteRepository.countKOVentes();
    }
}
