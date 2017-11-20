package com.danke.web;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * ElasticSearch Controller
 *
 * @date 2017年11月20日17:26:59
 */
@Component
@RequestMapping("//es")
public class ElasticSearchController {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建索引
     * http://localhost:8088/es/index
     *
     * @return
     */
    @RequestMapping("/index")
    @ResponseBody
    public IndexResponse index() {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        // Index Request
        // Providing the document source
        XContentBuilder builder = null;
        IndexResponse indexResponse = null;
        try {
            builder = XContentFactory.jsonBuilder();

            builder.startObject();
            {
                builder.field("user", "kimchy");
                builder.field("postDate", new Date());
                builder.field("message", "trying out Elasticsearch");
            }
            builder.endObject();
            IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
                    .source(builder);

            // Optional arguments 设置Requst参数

            // Synchronous Execution 同步调用
            indexResponse = client.index(indexRequest);

            // Asynchronous Execution 异步调用
            /*client.indexAsync(indexRequest, new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {

                }
                @Override
                public void onFailure(Exception e) {

                }
            });*/

            String index = indexResponse.getIndex();
            String type = indexResponse.getType();
            String id = indexResponse.getId();
            long version = indexResponse.getVersion();
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                logger.info("+++ CREATED");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                logger.info("+++ UPDATED");
            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                logger.info("+++ shardInfo.getTotal() != shardInfo.getSuccessful()");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                    logger.error("+++ " + reason);
                }
            }
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexResponse;

    }


    /**
     * 更新
     *
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public UpdateResponse update() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        UpdateResponse updateResponse = null;
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.field("updated", new Date());
                builder.field("reason", "daily update 222222");
            }
            builder.endObject();
            UpdateRequest request = new UpdateRequest("posts", "doc", "1")
                    .doc(builder);

            // Upserts 更新或者插入
//            String jsonString = "{\"created\":\"2017-01-01\"}";
//            request.upsert(jsonString, XContentType.JSON);

            // 同步调用
            updateResponse = client.update(request);

            // 异步调用
            /*client.updateAsync(request, new ActionListener<UpdateResponse>() {
                @Override
                public void onResponse(UpdateResponse updateResponse) {

                }

                @Override
                public void onFailure(Exception e) {

                }
            });*/

            String index = updateResponse.getIndex();
            String type = updateResponse.getType();
            String id = updateResponse.getId();
            long version = updateResponse.getVersion();
            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
                logger.info("+++ CREATED");
            } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                logger.info("+++ UPDATED");
            } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
                logger.info("+++ DELETED");
            } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
                logger.info("+++ NOOP"); // ??? 啥意思
            }

            // When the source retrieval is enabled in the UpdateRequest through the fetchSource method,
            // the response contains the source of the updated document
            if (updateResponse != null) {
                GetResult result = updateResponse.getGetResult();
                if (result != null) {
                    if (result.isExists()) { // 官网给的这个判定条件好像有问题啊，在此之前result就已经是null的情况下，这里会报控制针的错误
                        String sourceAsString = result.sourceAsString();
                        Map<String, Object> sourceAsMap = result.sourceAsMap();
                        byte[] sourceAsBytes = result.source();

                        logger.info("+++ sourceAsString");
                        logger.info("+++ sourceAsMap");
                        logger.info("+++ sourceAsBytes");
                    } else {
                        logger.info("+++ 没有返回对象");
                    }
                }
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateResponse;
    }

}
