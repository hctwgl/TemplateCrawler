package com.seveniu.entity.data;

import com.seveniu.entity.BaseRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@NoRepositoryBean
public interface DataRepository extends BaseRepository<Data, Long> {
}
