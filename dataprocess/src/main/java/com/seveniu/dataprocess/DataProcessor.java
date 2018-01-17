package com.seveniu.dataprocess;

import com.seveniu.dataprocess.processrecord.ProcessRecord;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
public interface DataProcessor {
    ProcessRecord.Result process(ProcessRecord processRecord);

    default String getName() {
        return this.getClass().getSimpleName();
    }

    int getOrder(); // 越小越靠前

    default boolean enable() {
        return true;
    } // 越小越靠前
}
