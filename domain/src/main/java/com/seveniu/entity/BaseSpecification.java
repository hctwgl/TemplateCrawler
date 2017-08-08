package com.seveniu.entity;


import com.seveniu.entity.base.BaseAuditableEntity;
import com.seveniu.entity.base.EntityStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.LinkedList;


/**
 * Created by seveniu on 3/5/17.
 * *
 */
final class BaseSpecification {
    private BaseSpecification() {
    }

    static <T extends BaseAuditableEntity> Specification<T> query(EntityStatus status, String likeField, String likeValue) {
        return (root, criteriaQuery, cb) -> {
            final Collection<Predicate> predicates = new LinkedList<>();
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (!StringUtils.isEmpty(likeField) && !StringUtils.isEmpty(likeValue)) {
                predicates.add(
                        cb.like(root.get(likeField), likeValue + "%")
                );
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    static <T extends BaseAuditableEntity> Specification<T> list(EntityStatus status) {
        return (root, criteriaQuery, cb) -> {
            final Collection<Predicate> predicates = new LinkedList<>();
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    static <T> Specification<T> query(String status) {
        return (root, criteriaQuery, cb) -> {
            if (status != null) {
                return cb.equal(root.get("status"), status);
            } else {
                return null;
            }
        };
    }
}
