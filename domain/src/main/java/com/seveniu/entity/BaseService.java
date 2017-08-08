package com.seveniu.entity;

import com.seveniu.entity.base.BaseAuditableEntity;
import com.seveniu.entity.base.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by seveniu on 2/6/17.
 * *
 */
public interface BaseService<T extends BaseAuditableEntity, ID> {
    T findOne(ID id);

    Iterable<T> findAll();

    Page<T> findAll(Pageable pageable);

    Page<T> query(EntityStatus status, String likeField, String likeValue, Pageable pageable);

    Page<T> list(EntityStatus status, Pageable pageable);

    T save(T t);

    void delete(ID id);

    void del(List<ID> ids);
}
