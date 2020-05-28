package com.company;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

import static java.lang.String.format;

@Component
@ConditionalOnProperty(name = "spring.cloud.consul.enabled", havingValue = "false")
public class StartupEventListener {

    private Logger log = LoggerFactory.getLogger(StartupEventListener.class);

    @Autowired
    private ConsulClient client;

    @Autowired
    private Environment environment;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String address = InetAddress.getLoopbackAddress().getHostAddress();
        int port = Integer.parseInt(environment.getProperty("server.port", "8080"));

        NewService service = new NewService();
        service.setName("my-service");
        service.setAddress(address);
        service.setPort(port);

        NewService.Check check = new NewService.Check();
        check.setHttp(format("http://%s:%s/actuator/health", address, port));
        check.setInterval("10s");
        service.setCheck(check);

        log.info("Register [{}]", service);

        var response = client.agentServiceRegister(service);

        log.info("Registered. Response: [{}]", response);
    }


}
