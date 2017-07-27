package com.seveniu.entity.data;

import com.seveniu.entity.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dhlz on 1/1/17.
 * *
 */
public class DataServiceImpl extends BaseServiceImpl<Data, Long> implements DataService {
    private final DataRepository dataRepository;

    @Autowired
    public DataServiceImpl(DataRepository dataRepository) {
        super(dataRepository);
        this.dataRepository = dataRepository;
    }


}
