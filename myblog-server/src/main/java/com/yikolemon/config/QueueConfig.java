package com.yikolemon.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class QueueConfig {

    //普通交换机名称
    public static final String  registExchange="registExchange";
    //普通队列名称
    public static final String  registQueue="registQueue";

    public static final String routingKey="send";

    //回复交换机
    public static final String replayExchange="replayExchange";
    public static final String  replayQueue="replayQueue";

    @Bean("registExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(registExchange);
    }

    @Bean("registQueue")
    public Queue queueA(){
        Map<String,Object> map=new HashMap<>(1);
        //设置ttl 单位ms,120s过期
        map.put("x-message-ttl",120000);
        return QueueBuilder.durable(registQueue).build();
    }

    @Bean
    public Binding queueABindingX(@Qualifier("registQueue") Queue queue, @Qualifier("registExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }


    @Bean("replayExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(replayExchange);
    }

    @Bean("replayQueue")
    public Queue queueB(){
        Map<String,Object> map=new HashMap<>(1);
        //设置ttl 单位ms,100分钟过期
        map.put("x-message-ttl",6000000);
        return QueueBuilder.durable(replayQueue).build();
    }

    @Bean
    public Binding queueBBindingY(@Qualifier("replayQueue") Queue queue, @Qualifier("replayExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}