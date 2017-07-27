package com.seveniu.entity.template;

import com.seveniu.entity.BaseRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@Repository
public interface TemplateRepository<T extends Template> extends BaseRepository<T, Long> {
}
