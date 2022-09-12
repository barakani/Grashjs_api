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
    private final ImageRepository imageRepository;

    public Image create(Image Image) {
        return imageRepository.save(Image);
    }

    public Image update(Image Image) {
        return imageRepository.save(Image);
    }

    public Collection<Image> getAll() { return imageRepository.findAll(); }

    public void delete(Long id){ imageRepository.deleteById(id);}

    public Optional<Image> findById(Long id) {return imageRepository.findById(id); }
}
