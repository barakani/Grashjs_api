package com.grash.service;

import com.grash.model.File;
import com.grash.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    private final ModelMapper modelMapper;

    public File create(File File) {
        return fileRepository.save(File);
    }

    public File update(File File) {
        return fileRepository.save(File);
    }

    public Collection<File> getAll() { return fileRepository.findAll(); }

    public void delete(Long id){ fileRepository.deleteById(id);}

    public Optional<File> findById(Long id) {return fileRepository.findById(id); }
}
