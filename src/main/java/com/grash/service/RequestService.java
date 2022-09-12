package com.grash.service;

import com.grash.model.Request;
import com.grash.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public Request create(Request Request) {
        return requestRepository.save(Request);
    }

    public Request update(Request Request) {
        return requestRepository.save(Request);
    }

    public Collection<Request> getAll() { return requestRepository.findAll(); }

    public void delete(Long id){ requestRepository.deleteById(id);}

    public Optional<Request> findById(Long id) {return requestRepository.findById(id); }
}
