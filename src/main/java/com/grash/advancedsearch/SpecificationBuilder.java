package com.grash.advancedsearch;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {
    private final List<FilterField> filterFields;

    public SpecificationBuilder() {
        this.filterFields = new ArrayList<>();
    }

    public final SpecificationBuilder<T> with(FilterField filterField) {
        filterFields.add(filterField);
        return this;
    }

    public Specification<T> build() {
        if (CollectionUtils.isEmpty(filterFields)) {
            return null;
        }
        //todo check this before get(0)
        Specification<T> result = new WrapperSpecification<>(filterFields.get(0));
        for (int idx = 1; idx < filterFields.size(); idx++) {
            FilterField criteria = filterFields.get(idx);
            result = Specification.where(result).and(new WrapperSpecification<>(criteria));
        }
        return result;
    }

}
