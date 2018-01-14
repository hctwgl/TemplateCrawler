package com.seveniu.manage.rest;

import com.seveniu.entity.BaseService;
import com.seveniu.entity.base.BaseAuditableEntity;
import com.seveniu.security.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Created by seveniu on 7/29/17.
 * *
 */
public class BaseApi<T extends BaseAuditableEntity, KEY> {
    protected BaseService<T, KEY> baseService;

    public BaseApi(BaseService<T, KEY> baseService) {
        this.baseService = baseService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    protected T add(@RequestBody T t) {
        Long curUserId = SecurityUtil.getCurrentUserId();
        t.setCreatedBy(curUserId);
        return baseService.save(t);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    protected void delete(@PathVariable("id") KEY id) {
        baseService.delete(id);
    }

    @RequestMapping(path = "/edit", method = RequestMethod.PUT)
    protected T edit(@RequestBody T t) {
        return baseService.save(t);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    protected T findOne(@PathVariable("id") KEY id) {
        return baseService.findOne(id);
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    protected Iterable<T> page() {
        return baseService.findAll();
    }

    @RequestMapping(path = "/page", method = RequestMethod.GET)
    protected Page<T> page(@PageableDefault Pageable pageable) {
        return baseService.findAll(pageable);
    }

    @RequestMapping(path = "/query", method = RequestMethod.GET)
    protected Page<T> query(@RequestParam(value = "field", defaultValue = "name") String field, @RequestParam("q") String q, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        if (StringUtils.isNotBlank(q)) {
            return baseService.query(null, field, q, pageable);
        } else {
            return new PageImpl<>(Collections.emptyList(), pageable, 0L);
        }
    }
}
