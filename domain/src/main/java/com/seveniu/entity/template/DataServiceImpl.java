package com.seveniu.entity.template;

import com.seveniu.entity.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dhlz on 1/1/17.
 * *
 */
public abstract class DataServiceImpl<T extends Template> extends BaseServiceImpl<T, Long> implements TemplateService<T> {
    private final TemplateRepository<T> templateRepository;

    @Autowired
    public DataServiceImpl(TemplateRepository<T> dataRepository) {
        super(dataRepository);
        this.templateRepository = dataRepository;
    }


}
