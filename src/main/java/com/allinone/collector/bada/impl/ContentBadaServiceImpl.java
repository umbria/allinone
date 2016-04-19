package com.allinone.collector.bada.impl;

import com.newrelic.api.agent.Trace;
import com.samsung.sse.common.util.HttpUtil;
import com.samsung.sse.common.xml.service.XMLCommonService;
import net.sf.ehcache.search.aggregator.AggregatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class ContentBadaServiceImpl implements ContentService {




    @Autowired
    private XMLCommonService commonService;


    @Value("${soccer.goal.mapping.url.team}")
    private String soccerGoalMappingTeamUrl;

    @Value("${soccer.goal.mapping.url.league}")
    private String soccerGoalMappingLeagueUrl;

    @Value("${soccer.opta.goal.mapping.match.url}")
    private String soccerOptaGoalMappingMatchUrl;

    @Value("${soccer.opta.goal.mapping.username}")
    private String soccerOptaGoalMappingUserName;

    @Value("${soccer.opta.goal.mapping.authkey}")
    private String soccerOptaGoalMappingAuthKey;

    @Value("${sse.agg.proxy.flag}")
    private String proxyTrueFalse;

    @Value("${sse.agg.proxy.hostname}")
    private String proxyHost;

    @Value("${sse.agg.proxy.port}")
    private String proxyPort;

    @Value("${bada.list.url}")
    private String badaListUrl;

    final private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Async("fileTransformerExecutor")
    @Trace(dispatcher = true)
    public void feedMapping() {
        logger.info("Starting Retrieval of Goal.com mapping Feed");
        try {

            createBadaList( );


        } catch (Exception ex) {
            logger.error("Exception occured: ", ex);

        }


        logger.info("Completed Retrieval of Goal.com mapping Feed");
    }

    /**
     * Retreive and send season mapping
     */
    private void createBadaList() {
        String url;



        url = badaListUrl;

        logger.debug("badaUrl: " + url);

        String result = readBada(url, proxyTrueFalse, proxyHost, proxyPort);
        try {
                System.out.println(result);

        } catch (AggregatorException ex) {
            logger.error("Error in parsing url: " + url, ex);

        }

    }

    public String readBada(String url, String proxyTrueFalse, String proxyHost, String proxyPort) {
        String response = HttpUtil.getHttpResponse(url, proxyTrueFalse, proxyHost, proxyPort);
        return response;
    }





}
