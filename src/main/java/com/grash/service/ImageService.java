package com.grash.service;

import com.grash.dto.ImagePatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.ImageMapper;
import com.grash.model.Company;
import com.grash.model.Image;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final CompanyService companyService;
    private final ImageMapper imageMapper;

    public Image create(Image Image) {
        return imageRepository.save(Image);
    }

    public Image update(Long id, ImagePatchDTO image) {
        if (imageRepository.existsById(id)) {
            Image savedImage = imageRepository.findById(id).get();
            return imageRepository.save(imageMapper.updateImage(savedImage, image));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Image> getAll() {
        return imageRepository.findAll();
    }

    public void delete(Long id) {
        imageRepository.deleteById(id);
    }

    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    public boolean hasAccess(User user, Image image) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(image.getCompany().getId());
    }

    public boolean canCreate(User user, Image imageReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(imageReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, imageMapper.toDto(imageReq));
    }

    public boolean canPatch(User user, ImagePatchDTO imageReq) {
        return true;
    }
}
