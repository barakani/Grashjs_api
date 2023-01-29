package com.grash.advancedsearch;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                Predicate result = cb.equal(root.get(filterField.getField()), filterField.getValue());
                if (filterField.getAlternatives() == null) {
                    return result;
                } else {
                    List<SpecificationBuilder<T>> specificationBuilders = filterField.getAlternatives().stream().map(alternative -> {
                        SpecificationBuilder<T> builder = new SpecificationBuilder<>();
                        builder.with(alternative);
                        return builder;
                    }).collect(Collectors.toList());
                    List<Predicate> predicates = specificationBuilders.stream().map(specificationBuilder -> specificationBuilder.build().toPredicate(root, query, cb)).collect(Collectors.toList());
                    predicates.add(result);
                    Predicate[] predicatesArray = predicates.toArray(new Predicate[0]);
                    return cb.or(predicatesArray);
                }
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
            case IN:
                CriteriaBuilder.In<Object> inClause = cb.in(root.get(filterField.getField()));
                filterField.getValues().forEach(inClause::value);
                return inClause;
            case IN_MANY_TO_MANY:
                Join<Object, Object> join = root.join(filterField.getField(), filterField.getJoinType());
                CriteriaBuilder.In<Object> inClause1 = cb.in(join.get("id"));
                filterField.getValues().forEach(inClause1::value);
                return inClause1;
        }
        return null;
    }
}
