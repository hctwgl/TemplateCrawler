package com.seveniu.entity.template;

import com.seveniu.entity.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@Service
public interface TemplateService<T extends Template> extends BaseService<T, Long> {


}
