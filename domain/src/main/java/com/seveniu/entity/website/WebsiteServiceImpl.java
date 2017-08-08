package com.seveniu.entity.website;

import com.seveniu.entity.BaseServiceImpl;
import com.seveniu.entity.Website;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by seveniu on 7/28/17.
 * *
 */
@Service
public class WebsiteServiceImpl extends BaseServiceImpl<Website, Long> implements WebsiteService {

    @Autowired
    public WebsiteServiceImpl(WebsiteRepository repository) {
        super(repository);
    }
}
