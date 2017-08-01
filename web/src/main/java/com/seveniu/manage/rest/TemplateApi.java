package com.seveniu.manage.rest;

import com.seveniu.entity.template.Template;
import com.seveniu.entity.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by seveniu on 7/27/17.
 * *
 */
@RestController
@RequestMapping("/api/template")
public class TemplateApi extends BaseApi<Template, Long> {
    private final TemplateService templateService;

    @Autowired
    public TemplateApi(TemplateService templateService) {
        super(templateService);
        this.templateService = templateService;
    }

}
