package com.irrah.techbackend.config;

import com.irrah.techbackend.domain.MessagePriority;
import com.irrah.techbackend.domain.MessageStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MessageStatusConverter());
        registry.addConverter(new MessagePriorityConverter());
    }

    private static class MessageStatusConverter implements Converter<String, MessageStatus> {
        @Override
        public MessageStatus convert(String source) {
            return MessageStatus.valueOf(source.toUpperCase());
        }
    }

    private static class MessagePriorityConverter implements Converter<String, MessagePriority> {
        @Override
        public MessagePriority convert(String source) {
            return MessagePriority.valueOf(source.toUpperCase());
        }
    }
}
