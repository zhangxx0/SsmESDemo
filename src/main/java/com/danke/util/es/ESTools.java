package com.danke.util.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * Elastic Search 工具类
 *
 * @author zhang.xx
 * @date 2017年12月15日18:33:46
 */
public class ESTools {

    public final static RestHighLevelClient client = build();

    private static RestHighLevelClient build() {
        if (client != null) {
            return client;
        }
        RestHighLevelClient client = null;
        try {
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("localhost", 9200, "http")));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return client;
    }

    /**
     * 关闭
     */
    public static void close() {
        if (null != client) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
