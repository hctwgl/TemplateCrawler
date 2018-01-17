package com.seveniu.dataprocess.processrecord;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
@Repository
public interface ProcessRecordRepository extends ProcessRepository<ProcessRecord> {

    ProcessRecord findFirstByOrderByIdAsc();

    void deleteByDataId(Long dataId);

    @Modifying
    @Query("update ProcessRecord p set p.processStatus =:processStatus where p.id =:id")
    void updateProcessStatus(@Param("processStatus") ProcessStatus processStatus, @Param("id") Long id);
}
