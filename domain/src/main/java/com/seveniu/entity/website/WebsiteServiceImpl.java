package com.seveniu.entity.website;

import com.seveniu.entity.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by seveniu on 7/28/17.
 * *
 */
@Service
public class WebsiteServiceImpl extends BaseServiceImpl<Website, Long> implements WebsiteService {
    private WebsiteRepository websiteRepository;

    @Autowired
    public WebsiteServiceImpl(WebsiteRepository repository) {
        super(repository);
        this.websiteRepository = repository;
    }

    @Override
    public Website findOneByDomain(String domain) {
        return websiteRepository.findByDomain(domain);
    }
}
