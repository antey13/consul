package com.company;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsulConfig {

    @Bean
    public ConsulClient consulClient() {
        return new ConsulClient("localhost");
    }

}
