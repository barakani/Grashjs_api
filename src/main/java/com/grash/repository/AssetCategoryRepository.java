package com.grash.repository;

import com.grash.model.AssetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long> {

    Collection<AssetCategory> findByCompanySettings(Long id);
}
