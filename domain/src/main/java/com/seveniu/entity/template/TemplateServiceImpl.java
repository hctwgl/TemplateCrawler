package com.seveniu.entity.template;

import com.seveniu.entity.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dhlz on 1/1/17.
 * *
 */
public class TemplateServiceImpl extends BaseServiceImpl<Template, Long> implements TemplateService {
    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateServiceImpl(TemplateRepository dataRepository) {
        super(dataRepository);
        this.templateRepository = dataRepository;
    }


}
