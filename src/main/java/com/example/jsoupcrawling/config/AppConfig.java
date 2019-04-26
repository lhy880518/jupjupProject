package com.example.jsoupcrawling.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:static/properties/secureInfo.properties")
public class AppConfig {
}
