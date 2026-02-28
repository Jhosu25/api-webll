package com.itsqmet.service;

import com.itsqmet.entity.Vinilo;
import com.itsqmet.repository.ViniloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ViniloService {

    @Autowired
    private ViniloRepository viniloRepository;

    public List<Vinilo> getVinilos() {
        return viniloRepository.findAll();
    }

    public Vinilo getViniloById(Long id) {
        return viniloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vinilo no encontrado"));
    }

    public Vinilo createVinilo(Vinilo vinilo) {
        return viniloRepository.save(vinilo);
    }

    public Vinilo updateVinilo(Long id, Vinilo vinilo) {

        Vinilo viniloExistente = viniloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vinilo no encontrado"));

        viniloExistente.setTitulo(vinilo.getTitulo());
        viniloExistente.setArtista(vinilo.getArtista());
        viniloExistente.setAnio(vinilo.getAnio());
        viniloExistente.setPrecio(vinilo.getPrecio());
        viniloExistente.setStock(vinilo.getStock());

        return viniloRepository.save(viniloExistente);
    }

    public void deleteVinilo(Long id) {

        Vinilo vinilo = viniloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vinilo no encontrado"));

        viniloRepository.delete(vinilo);
    }
}