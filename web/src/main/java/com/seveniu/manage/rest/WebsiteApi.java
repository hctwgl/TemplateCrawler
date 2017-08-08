package com.seveniu.manage.rest;

import com.seveniu.entity.Website;
import com.seveniu.entity.website.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by seveniu on 7/27/17.
 * *
 */
@RestController
@RequestMapping("/api/website")
public class WebsiteApi extends BaseApi<Website, Long> {
    private final WebsiteService websiteService;

    @Autowired
    public WebsiteApi(WebsiteService websiteService) {
        super(websiteService);
        this.websiteService = websiteService;
    }

}
