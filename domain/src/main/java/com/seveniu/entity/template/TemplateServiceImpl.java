package com.seveniu.entity.template;

import com.seveniu.entity.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by dhlz on 1/1/17.
 * *
 */
@Service
public class TemplateServiceImpl extends BaseServiceImpl<Template, Long> implements TemplateService {
    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateServiceImpl(TemplateRepository dataRepository) {
        super(dataRepository);
        this.templateRepository = dataRepository;
    }


    @Override
    public Page<Template> findByWebsite(Long websiteId, Pageable pageable) {
        return templateRepository.findByWebsiteId(websiteId, pageable);
    }
}
