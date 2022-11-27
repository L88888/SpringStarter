package com.sailing.commons.data.es.starter;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.CheckedConsumer;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;

import java.io.IOException;
import java.util.List;

/**
 * @program: spring-starter
 * @description: SailingRestHighLevelClient elasticsearch 子类实现
 * @author: LIULEI
 * @create: 2020-08-05 19:06:
 **/
public class SailingRestHighLevelClient extends RestHighLevelClient {

    public SailingRestHighLevelClient(RestClientBuilder restClientBuilder) {
        super(restClientBuilder);
    }

    protected SailingRestHighLevelClient(RestClientBuilder restClientBuilder, List<NamedXContentRegistry.Entry> namedXContentEntries) {
        super(restClientBuilder, namedXContentEntries);
    }

    protected SailingRestHighLevelClient(RestClient restClient, CheckedConsumer<RestClient, IOException> doClose,
                                         List<NamedXContentRegistry.Entry> namedXContentEntries) {
        super(restClient, doClose, namedXContentEntries);
    }
}
