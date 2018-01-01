package com.seveniu.entity.website;

import com.seveniu.entity.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created by seveniu on 7/28/17.
 * *
 */
@Service
public interface WebsiteService extends BaseService<Website, Long> {
    Website findOneByDomain(String domain);
}
