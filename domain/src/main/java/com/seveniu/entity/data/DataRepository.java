package com.seveniu.entity.data;

import com.seveniu.entity.BaseRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@Repository
public interface DataRepository extends BaseRepository<Data, Long> {
}
