package com.grash.dto.analytics.parts;

import com.grash.dto.AssetMiniDTO;
import com.grash.dto.PartMiniDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartConsumptionsByAsset extends PartMiniDTO {
    private long cost;
}
