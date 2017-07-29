package com.seveniu.manage.rest;

import com.seveniu.entity.BaseAuditableEntity;
import com.seveniu.entity.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by seveniu on 7/29/17.
 * *
 */
public class BaseApi<T extends BaseAuditableEntity, KEY> {
    private BaseService<T, KEY> baseService;

    public BaseApi(BaseService<T, KEY> baseService) {
        this.baseService = baseService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    private T add(@RequestBody T t) {
        return baseService.save(t);
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    private void delete(@RequestParam("id") KEY id) {
        baseService.delete(id);
    }

    @RequestMapping(path = "/edit", method = RequestMethod.PUT)
    private T edit(@RequestBody T t) {
        return baseService.save(t);
    }

    @RequestMapping(path = "/page", method = RequestMethod.GET)
    private Page<T> page(@PageableDefault Pageable pageable) {
        return baseService.findAll(pageable);
    }
}
