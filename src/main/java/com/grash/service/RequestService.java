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
    private final RequestRepository RequestRepository;

    public Request create(Request Request) {
        return RequestRepository.save(Request);
    }

    public Request update(Request Request) {
        return RequestRepository.save(Request);
    }

    public Collection<Request> getAll() { return RequestRepository.findAll(); }

    public void delete(Long id){ RequestRepository.deleteById(id);}

    public Optional<Request> findById(Long id) {return RequestRepository.findById(id); }
}
