package com.seveniu.entity.data;

import com.seveniu.entity.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dhlz on 1/1/17.
 * *
 */
@Service
public class DataServiceImpl extends BaseServiceImpl<Data, Long> implements DataService {
    private final DataRepository dataRepository;

    @Autowired
    public DataServiceImpl(DataRepository dataRepository) {
        super(dataRepository);
        this.dataRepository = dataRepository;
    }


    @Override
    public Data getFirst() {
        return dataRepository.findFirstByOrderByIdAsc();
    }
}
