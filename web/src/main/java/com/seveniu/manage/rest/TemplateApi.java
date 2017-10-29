package com.seveniu.manage.rest;

import com.seveniu.entity.template.Template;
import com.seveniu.entity.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(path = "/website/{websiteId}/page", method = RequestMethod.GET)
    protected Page<Template> pageBySite(@PathVariable("websiteId") Long websiteId, @PageableDefault Pageable pageable) {
        return templateService.findByWebsite(websiteId, pageable);
    }
}
