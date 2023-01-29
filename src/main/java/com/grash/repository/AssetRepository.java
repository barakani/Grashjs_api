package com.grash.repository;

import com.grash.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface AssetRepository extends JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {
    Collection<Asset> findByCompany_Id(Long id);

    Collection<Asset> findByParentAsset_Id(Long id);

    Collection<Asset> findByLocation_Id(Long id);
}

