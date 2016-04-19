package com.allinone.processor.handler.impl;

import com.allinone.processor.handler.AggregatorMessageListener;
import com.newrelic.api.agent.Trace;
import com.samsung.sse.common.dto.AggregatorMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * Process contens that collected from collector
 */
@Service
    public class ContentMessageListenerImpl implements AggregatorMessageListener {
        private Logger logger = LoggerFactory.getLogger(this.getClass());

//        @Autowired
//        private ContentBadaMessageService scrF1MessageService;







        @Override
        @Trace(dispatcher = true)
        public void handleAggregatorObject(AggregatorMessageDTO message) throws IOException {
            logger.info("Started Handling received message: type: " + message.getType() + " fileName: " + message.getFileName());
            if (logger.isDebugEnabled()) {
                //logger.debug("Handling aggregatorMessageDTO: " + message);
            }
//            if (message.getType().equals(SoccerConstants.OPTA_SCR_OMO_SERVICE_TYPE_F1)) {
//                scrF1MessageService.handleMessage(message);
//            }
//            else if (message.getType().equals(SoccerConstants.OPTA_SCR_OMO_SERVICE_TYPE_F2)) {
//                scrF2MessageService.handleMessage(message);
//            }

            logger.info("Completed Handling received message: type: " + message.getType() + " fileName: " + message.getFileName());
        }
    }
