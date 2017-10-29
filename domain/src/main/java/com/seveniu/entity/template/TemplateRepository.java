package com.seveniu.entity.template;

import com.seveniu.entity.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@Repository
public interface TemplateRepository extends BaseRepository<Template, Long> {

    Page<Template> findByWebsiteId(Long websiteId, Pageable pageable);
}
