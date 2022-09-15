package com.grash.service;

import com.grash.dto.GeneralPreferencesDTO;
import com.grash.exception.CustomException;
import com.grash.model.AssetCategory;
import com.grash.model.GeneralPreferences;
import com.grash.repository.GeneralPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralPreferencesService {
    private final GeneralPreferencesRepository generalPreferencesRepository;
    private final ModelMapper modelMapper;

    public GeneralPreferences create(GeneralPreferences GeneralPreferences) {
        return generalPreferencesRepository.save(GeneralPreferences);
    }

    public GeneralPreferences update(Long id, GeneralPreferencesDTO GeneralPreferencesDto) {
        if (generalPreferencesRepository.existsById(id)) {
            GeneralPreferences savedGeneralPreferences = generalPreferencesRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(GeneralPreferencesDto, savedGeneralPreferences);
            return generalPreferencesRepository.save(savedGeneralPreferences);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<GeneralPreferences> getAll() { return generalPreferencesRepository.findAll(); }

    public void delete(Long id){ generalPreferencesRepository.deleteById(id);}

    public Optional<GeneralPreferences> findById(Long id) {return generalPreferencesRepository.findById(id); }

    public Collection<GeneralPreferences> findByCompanySettings(Long id) {
        return generalPreferencesRepository.findByCompanySettings_Id(id);
    }
}
