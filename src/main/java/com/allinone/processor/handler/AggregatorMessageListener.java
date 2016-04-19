package com.allinone.processor.handler;

import com.samsung.sse.common.dto.AggregatorMessageDTO;

import java.io.IOException;

/**
 * Created by youngseokkim on 12.04.2016.
 */
public interface AggregatorMessageListener {
    void handleAggregatorObject(AggregatorMessageDTO message) throws IOException;
}

