package com.danke.util.singleton;

/**
 * ES 客户端单例
 *
 * 没用，，，想错了，这个单例不能用来提供Client
 * @date 2017年12月11日17:10:23
 */
public class EsClientSingleton {

    private static EsClientSingleton singleton;
    private EsClientSingleton(){};

    public static EsClientSingleton getEcClient() {
        if (singleton == null) {
            singleton = new EsClientSingleton();
        }
        return singleton;
    }

}
