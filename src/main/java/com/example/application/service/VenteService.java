package com.example.application.service;

import com.example.application.models.User;
import com.example.application.models.Vente;
import com.example.application.repository.VenteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VenteService {
    private final VenteRepository venteRepository;

    public VenteService(VenteRepository venteRepository) {
        this.venteRepository = venteRepository;
    }

    public List<Vente> allVentes(){
        return venteRepository.findAll();
    }

    public void save(Vente v ){
        venteRepository.save(v);
    }

    public List<Vente> getFilteredVentes(String filterOption , User agent) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        LocalDate today = LocalDate.now();
        if(filterOption.equals("All")) {
            startDate = today.minusDays(1000);
            endDate = today;
        }
        else if (filterOption.equals("Last 15 days")) {
            startDate = today.minusDays(15);
            endDate = today;
        } else if (filterOption.equals("This month")) {
            startDate = today.withDayOfMonth(1);
            endDate = today;
        } else if (filterOption.equals("Last 3 months")) {
            startDate = today.minusMonths(3).withDayOfMonth(1);
            endDate = today;
        }
        return venteRepository.findByDateVenteBetween(startDate, endDate , agent);
    }

    public List<Vente> getFilteredVentesAll(String filterOption) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        LocalDate today = LocalDate.now();
        if(filterOption.equals("All")) {
            startDate = today.minusDays(1000);
            endDate = today;
        }
        else if (filterOption.equals("Last 15 days")) {
            startDate = today.minusDays(15);
            endDate = today;
        } else if (filterOption.equals("This month")) {
            startDate = today.withDayOfMonth(1);
            endDate = today;
        } else if (filterOption.equals("Last 3 months")) {
            startDate = today.minusMonths(3).withDayOfMonth(1);
            endDate = today;
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
    public long countVentesAccepté(){
        return  venteRepository.countVentesByQualification("Accepté");
    }
    public long countVentesEn_Attente(){
        return  venteRepository.countVentesByQualification("En Attente");
    }
    public long countVentesEffectif(){
        return  venteRepository.countVentesByQualification("Effectif");
    }
    public long countVentesKO(){
        return  venteRepository.countVentesByQualification("KO");
    }

    public long countVentesByAgent(User agent){
        return venteRepository.countByAgent(agent);
    }

}
