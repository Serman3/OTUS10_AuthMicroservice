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
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class JwtClientConfig {

    @Bean
    public OkHttpClient jwtClient() {
        return new OkHttpClient();
    }

    @Bean
    public Retryer jwtRetryer() {
        return new Retryer.Default(100, 2000, 7);
    }

    @Bean
    public Decoder jwtFeignDecoder() {
        var jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    @Bean
    public ErrorDecoder jwtErrorDecoder() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public Encoder jwtFeignEncoder() {
        return new JacksonEncoder();
    }

    public ObjectMapper customObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public Logger.Level jwtFeignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor jwtRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Accept", "*/*");
            requestTemplate.header("Content-Type", "application/json");
        };
    }

}
