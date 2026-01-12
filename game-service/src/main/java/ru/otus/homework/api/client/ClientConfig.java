package ru.otus.homework.api.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class ClientConfig {

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 2000, 7);
    }

    @Bean
    public Decoder feignDecoder() {
        var jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder();
    }

    public ObjectMapper customObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Accept", "*/*");
            requestTemplate.header("Content-Type", "application/json");
        };
    }

}
