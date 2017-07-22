package com.seveniu.entity.task;

import com.seveniu.entity.BaseRepository;
import com.seveniu.entity.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@Repository
public interface TaskRepository extends BaseRepository<Task, Long> {
    @Query("SELECT t FROM CrawlerTask t WHERE t.cycle.id > 0 AND t.nextRunTime < now() AND t.status =:status AND t.runStatus =:runStatus")
    List<Task> getDueTask(@Param("status") EntityStatus status, @Param("runStatus") TaskRunStatus runStatus);

    @Modifying
    @Transactional
    @Query("UPDATE CrawlerTask t set t.runStatus='DONE' WHERE t.runStatus='ACTIVE'")
    void resetRunningTaskToUnRun();

    Page<Task> findByCreateUserId(Long currentUserId, Pageable pageable);
}
