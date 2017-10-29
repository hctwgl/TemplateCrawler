package com.seveniu.manage.rest;

import com.seveniu.entity.Website;
import com.seveniu.entity.website.WebsiteService;
import com.seveniu.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by seveniu on 7/27/17.
 * *
 */
@RestController
@CrossOrigin
@RequestMapping("/api/website")
public class WebsiteApi extends BaseApi<Website, Long> {
    private final WebsiteService websiteService;

    @Autowired
    public WebsiteApi(WebsiteService websiteService) {
        super(websiteService);
        this.websiteService = websiteService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public Website add(@RequestBody Website t) {
        String domain = t.getDomain();
        Website website = websiteService.findOneByDomain(domain);
        if (website == null) {
            Long curUserId = SecurityUtil.getCurrentUserId();
            t.setCreatedBy(curUserId);
            return websiteService.save(t);
        }
        return website;
    }

    @RequestMapping("domain")
    public Website getByDomain(@RequestParam("value") String domain) {
        Website website = this.websiteService.findOneByDomain(domain);
        System.out.println(website);
        return website;
    }

}
