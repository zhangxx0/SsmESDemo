package com.danke.web;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
     * 新增doc的操作也是在这个接口中进行应该，老版本的是这样的
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
                builder.field("user", "zxx");
                builder.field("postDate", new Date());
                builder.field("message", "trying out Elasticsearch");
            }
            builder.endObject();
            // 不加id的话，自动生成id
            IndexRequest indexRequest = new IndexRequest("posts", "doc", "2")
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
     * http://localhost:8088/es/update
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


    /**
     * GET
     * http://localhost:8088/es/get
     *
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public GetResponse get() {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        GetRequest getRequest = new GetRequest("posts", "doc", "2");
        GetResponse getResponse = null;

        try {
            // 同步执行
            getResponse = client.get(getRequest);

            String index = getResponse.getIndex();
            String type = getResponse.getType();
            String id = getResponse.getId();
            logger.info("+++ index:" + index + " type:" + type + " id:" + id);
            if (getResponse.isExists()) {
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString();
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                byte[] sourceAsBytes = getResponse.getSourceAsBytes();
                logger.info("+++  " + version + " " + sourceAsString);
            } else {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResponse;

    }


    /**
     * Search API
     *
     * @return
     */
    @RequestMapping("/search")
    @ResponseBody
    public SearchResponse search(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        //
        SearchRequest searchRequest = new SearchRequest();
        SearchResponse searchResponse = null;
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.termQuery("user", "kimchy"));
//            searchSourceBuilder.from(0);
//            searchSourceBuilder.size(3);
//            searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("user", "kimchy");
//        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
//        matchQueryBuilder.prefixLength(3);
//        matchQueryBuilder.maxExpansions(10);

            searchSourceBuilder.query(matchQueryBuilder);

            searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));

            // Source filtering 这个东西是干嘛用的？ 2017年11月21日11:53:59
            // searchSourceBuilder.fetchSource(false);

            // Requesting Highlighting
            /**
             * 若添加下面这段设置高亮的代码的话，就会报下面的错误,搜索了半天没有找到结果，暂时搁置在这里，之后用到再来研究解决；主要是文档里写的也不是很清楚，很难受；2017年11月21日15:40:35
             * HTTP Status 500 - Could not write content: No serializer found for class org.elasticsearch.common.text.Text and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) ) (through reference chain: org.elasticsearch.action.search.SearchResponse["hits"]->org.elasticsearch.search.SearchHits["hits"]->org.elasticsearch.search.SearchHit["highlightFields"]->java.util.HashMap["user"]->org.elasticsearch.search.fetch.subphase.highlight.HighlightField["fragments"]); nested exception is com.fasterxml.jackson.databind.JsonMappingException: No serializer found for class org.elasticsearch.common.text.Text and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) ) (through reference chain: org.elasticsearch.action.search.SearchResponse["hits"]->org.elasticsearch.search.SearchHits["hits"]->org.elasticsearch.search.SearchHit["highlightFields"]->java.util.HashMap["user"]->org.elasticsearch.search.fetch.subphase.highlight.HighlightField["fragments"])
             */
            HighlightBuilder highlightBuilder = new HighlightBuilder();
//            HighlightBuilder.Field highlightTitle =
//                    new HighlightBuilder.Field("title");
//            highlightTitle.highlighterType("unified");
//            highlightBuilder.field(highlightTitle);
            HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("user");
            highlightBuilder.field(highlightUser);
            searchSourceBuilder.highlighter(highlightBuilder);

            // Requesting Aggregations
            // Requesting Suggestions
            //

            searchRequest.source(searchSourceBuilder);

            // 同步执行
            searchResponse = client.search(searchRequest);

            RestStatus status = searchResponse.status();
            TimeValue took = searchResponse.getTook();
            Boolean terminatedEarly = searchResponse.isTerminatedEarly();
            boolean timedOut = searchResponse.isTimedOut();

            logger.info("+++ " + status.toString() + " " + took.toString() + " " + terminatedEarly + " " + timedOut);

            int totalShards = searchResponse.getTotalShards();
            int successfulShards = searchResponse.getSuccessfulShards();
            int failedShards = searchResponse.getFailedShards();
            for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
                // failures should be handled here
            }

            // Retrieving SearchHits
            SearchHits hits = searchResponse.getHits();
            long totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();
            SearchHit[] searchHits = hits.getHits();
            logger.info("+++ " + totalHits + " " + maxScore);

            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                String index = hit.getIndex();
                String type = hit.getType();
                String id = hit.getId();
                float score = hit.getScore();
                logger.info("+++ " + index + " " + type + " " + id + " " + score);

                String sourceAsString = hit.getSourceAsString();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//                String documentTitle = (String) sourceAsMap.get("title");
//                List<Object> users = (List<Object>) sourceAsMap.get("user");
//                Map<String, Object> innerObject = (Map<String, Object>) sourceAsMap.get("innerObject");

                logger.info("+++ " + sourceAsString);

                // Retrieving Highlighting
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get("user");
                if (highlight != null) {
                    Text[] fragments = highlight.fragments();
                    String fragmentString = fragments[0].string();
                    logger.info("+++ highlight " + fragmentString);
                }
            }
            // Retrieving Aggregations
            // Retrieving Suggestions
            // Retrieving Profiling Results

        } catch (IOException e) {
            logger.info("+++ bao cuo la ");
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("+++ bao cuo la 2");
            e.printStackTrace();
        }
        logger.info("+++ before return ");

        return searchResponse;
    }

    /**
     * 分页Search
     * 用于处理大量数据返回
     *
     * @return
     */
    @RequestMapping("/scollSearch")
    @ResponseBody
    public SearchResponse scollSearch() throws IOException {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        // (1)Initialize the search scroll context
        // (2)Retrieve all the relevant documents
        // (3)Clear the scroll context

        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest searchRequest = new SearchRequest("posts");
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = client.searchScroll(scrollRequest);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();

            logger.info("+++ scrollId:" +scrollId);
            logger.info("+++ searchHits:" +searchHits.toString());
        }

        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest);
        boolean succeeded = clearScrollResponse.isSucceeded();

        logger.info("+++ ClearScrollRequest success:" + succeeded);

        return searchResponse;
    }


}
