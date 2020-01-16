package com.changgou.search.listener;

import com.changgou.search.config.RabbitMQConfig;
import com.changgou.search.service.ESManagerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsUpListener {

    @Autowired
    private ESManagerService esManagerService;

    @RabbitListener(queues = RabbitMQConfig.SEARCH_ADD_QUEUE)
    public void receiveMessage(String message){
        System.out.println("消息是:"+message);
        esManagerService.importSkuListBySpuId(message);
    }

    @RabbitListener(queues = RabbitMQConfig.SEARCH_DEL_QUEUE)
    public void delMessage(String message){
        System.out.println("spuId:"+message);
        esManagerService.delSkuListBySpuId(message);
    }
}
