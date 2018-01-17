package com.seveniu.dataprocess.processrecord;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
public interface ProcessRepository<T extends ProcessRecord> extends JpaRepository<T, Long> {
    T findFirstByOrderByIdAsc();
    void deleteByDataId(Long dataId);
}
