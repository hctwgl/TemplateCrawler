package com.seveniu.entity.template;

import com.seveniu.entity.BaseRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@NoRepositoryBean
public interface TemplateRepository<T extends Template> extends BaseRepository<T, Long> {
}
