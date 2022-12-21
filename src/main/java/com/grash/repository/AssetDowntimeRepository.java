package com.grash.repository;

import com.grash.model.AssetDowntime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface AssetDowntimeRepository extends JpaRepository<AssetDowntime, Long> {

    Collection<AssetDowntime> findByAsset_Id(Long id);
}
