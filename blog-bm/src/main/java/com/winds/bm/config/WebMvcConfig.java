package com.winds.bm.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.winds.bm.common.interceptor.ClearTokenInteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Autowired
    private CorsFilter corsFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(clearTokenInteceptor()).addPathPatterns("/**");
        registry.addInterceptor(corsFilter).addPathPatterns("/**");
    }

    @Bean
    public ClearTokenInteceptor clearTokenInteceptor() {
        ClearTokenInteceptor clearTokenInteceptor = new ClearTokenInteceptor();
        return clearTokenInteceptor;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect
        );


        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

        fastConverter.setFastJsonConfig(fastJsonConfig);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        converters.add(fastConverter);
    }


    @Bean
    public FastJsonViewResponseBodyAdvice FastJsonViewResponseBodyAdvice() {
        FastJsonViewResponseBodyAdvice advice = new FastJsonViewResponseBodyAdvice();
        return advice;
    }
}
