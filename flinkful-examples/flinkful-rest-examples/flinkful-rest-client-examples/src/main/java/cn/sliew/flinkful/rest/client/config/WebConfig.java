package cn.sliew.flinkful.rest.client.config;

import cn.sliew.flinkful.rest.client.message.MappingFlinkShadedJackson2HttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new MappingFlinkShadedJackson2HttpMessageConverter());
    }
}
