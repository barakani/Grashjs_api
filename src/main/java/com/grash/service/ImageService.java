package com.grash.service;

import com.grash.model.Image;
import com.grash.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository ImageRepository;

    public Image create(Image Image) {
        return ImageRepository.save(Image);
    }

    public Image update(Image Image) {
        return ImageRepository.save(Image);
    }

    public Collection<Image> getAll() { return ImageRepository.findAll(); }

    public void delete(Long id){ ImageRepository.deleteById(id);}

    public Optional<Image> findById(Long id) {return ImageRepository.findById(id); }
}
