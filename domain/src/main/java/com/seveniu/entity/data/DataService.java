package com.seveniu.entity.data;

import com.seveniu.entity.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
public interface DataService extends BaseService<Data, Long> {

    Data getFirst();

}
