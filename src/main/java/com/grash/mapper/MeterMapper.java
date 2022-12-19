package com.grash.mapper;

import com.grash.dto.MeterPatchDTO;
import com.grash.dto.MeterShowDTO;
import com.grash.model.Meter;
import com.grash.model.Reading;
import com.grash.service.ReadingService;
import com.grash.utils.AuditComparator;
import com.grash.utils.Helper;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, AssetMapper.class, UserMapper.class})
public interface MeterMapper {
    Meter updateMeter(@MappingTarget Meter entity, MeterPatchDTO dto);

    @Mappings({})
    MeterPatchDTO toDto(Meter model);

    MeterShowDTO toShowDto(Meter model, @Context ReadingService readingService);

    @AfterMapping
    default MeterShowDTO toShowDto(Meter model, @MappingTarget MeterShowDTO target, @Context ReadingService readingService) {
        Collection<Reading> readings = readingService.findByMeter(target.getId());
        if (!readings.isEmpty()) {
            Reading lastReading = Collections.min(readings, new AuditComparator());
            target.setLastReading(lastReading.getCreatedAt());
            Date nextReading = Helper.incrementDays(Date.from(lastReading.getCreatedAt()), target.getUpdateFrequency());
            target.setNextReading(nextReading.toInstant());
        }
        return target;
    }

}
