package com.grash.advancedsearch;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;

public class WrapperSpecification<T> implements Specification<T> {

    private final FilterField filterField;

    public WrapperSpecification(final FilterField filterField) {
        super();
        this.filterField = filterField;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        String strToSearch = filterField.getValue().toString().toLowerCase();

        switch (Objects.requireNonNull(SearchOperation.getSimpleOperation(filterField.getOperation()))) {
            case CONTAINS:
                return cb.like(cb.lower(root.get(filterField.getField())), "%" + strToSearch + "%");
            case DOES_NOT_CONTAIN:
                return cb.notLike(cb.lower(root.get(filterField.getField())), "%" + strToSearch + "%");
            case BEGINS_WITH:
                return cb.like(cb.lower(root.get(filterField.getField())), strToSearch + "%");
            case DOES_NOT_BEGIN_WITH:
                return cb.notLike(cb.lower(root.get(filterField.getField())), strToSearch + "%");
            case ENDS_WITH:
                return cb.like(cb.lower(root.get(filterField.getField())), "%" + strToSearch);
            case DOES_NOT_END_WITH:
                return cb.notLike(cb.lower(root.get(filterField.getField())), "%" + strToSearch);
            case EQUAL:
                return cb.equal(root.get(filterField.getField()), filterField.getValue());
            case NOT_EQUAL:
                return cb.notEqual(root.get(filterField.getField()), filterField.getValue());
            case NUL:
                return cb.isNull(root.get(filterField.getField()));
            case NOT_NULL:
                return cb.isNotNull(root.get(filterField.getField()));
            case GREATER_THAN:
                return cb.greaterThan(root.get(filterField.getField()), filterField.getValue().toString());
            case GREATER_THAN_EQUAL:
                return cb.greaterThanOrEqualTo(root.get(filterField.getField()), filterField.getValue().toString());
            case LESS_THAN:
                return cb.lessThan(root.get(filterField.getField()), filterField.getValue().toString());
            case LESS_THAN_EQUAL:
                return cb.lessThanOrEqualTo(root.get(filterField.getField()), filterField.getValue().toString());
        }
        return null;
    }
}
