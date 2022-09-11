package com.grash.service;

import com.grash.model.Location;
import com.grash.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository LocationRepository;

    private final ModelMapper modelMapper;

    public Location create(Location Location) {
        return LocationRepository.save(Location);
    }

    public Location update(Location Location) {
        return LocationRepository.save(Location);
    }

    public Collection<Location> getAll() { return LocationRepository.findAll(); }

    public void delete(Long id){ LocationRepository.deleteById(id);}

    public Optional<Location> findById(Long id) {return LocationRepository.findById(id); }
}
