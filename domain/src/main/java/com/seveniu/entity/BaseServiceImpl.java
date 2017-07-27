package com.seveniu.entity;

import com.seveniu.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by seveniu on 2/6/17.
 * *
 */
public abstract class BaseServiceImpl<T extends BaseAuditableEntity, ID extends Serializable> implements BaseService<T, ID> {
    private final BaseRepository<T, ID> repository;

    public BaseServiceImpl(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T findOne(ID id) {
        return repository.findOne(id);
    }

    @Override
    public Iterable<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<T> query(EntityStatus status, String likeField, String likeValue, Pageable pageable) {
        return repository.findAll(BaseSpecification.query(status, likeField, likeValue), pageable);
    }

    @Override
    public T save(T t) {
        Long userId = SecurityUtil.getCurrentUserId();
//        Long userId = 1L;
        if (t.getId() != null) { // update
            t.setUpdatedBy(userId);
            t.setTimeUpdated(new Date());
        } else {
            t.setCreatedBy(userId); // create
            t.setTimeCreated(new Date());
            t.setStatus(EntityStatus.ACTIVE);
        }
        return repository.save(t);
    }

    @Override
    public void delete(ID id) {
//        T t = repository.findOne(id);
//        if (t != null) {
//            t.setStatus(EntityStatus.DELETED);
//            t.setTimeDeleted(new Date());
//        }
//        repository.save(t);
        repository.delete(id);
    }

    @Override
    public void del(List<ID> ids) {
        repository.deleteByIdIn(ids);
    }

    @Override
    public Page<T> list(EntityStatus status, Pageable pageable) {
        return repository.findAll(BaseSpecification.list(status), pageable);
    }
}
