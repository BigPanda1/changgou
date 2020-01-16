package com.changgou.business.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RabbitMqListener {

    @Autowired
    private RestTemplate restTemplate;

    @RabbitListener(queues ="ad_update_queue")
    public void recive(String message){

        System.out.println("接受到的消息是:"+message);

        String url = "http://192.168.200.128/ad_update?position="+message;
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(url, Map.class);

        Map map = forEntity.getBody();
        System.out.println(map);
    }
}
