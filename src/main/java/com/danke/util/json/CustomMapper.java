package com.danke.util.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 重写SpringMVC里默认序列化类
 *
 */
public class CustomMapper extends ObjectMapper {
    public CustomMapper() {
        this.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
