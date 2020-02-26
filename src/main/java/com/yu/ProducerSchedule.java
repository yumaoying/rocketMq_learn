package com.yu;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;

/**
 * Created by yumaoying
 * 生产者-延迟发送
 */
public class ProducerSchedule {

    public static void main(String[] args) throws Exception {
        //1.创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("my_pGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");
        //启动生产者
        producer.start();
        System.out.println("延迟消息发送==============");
        long startTime = System.currentTimeMillis();
        Message message = new Message("myTopic002", "延迟消息1".getBytes());
        //设置broker.conf,添加延迟配置
        // messageDelayLevel	1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        //这个配置项配置了从1级开始，各级延时的时间，可以修改这个指定级别的延时时间；
        //设置消息指定级别为1，表示消息在在1秒后发送
        message.setDelayTimeLevel(1);
        SendResult sendResult = producer.send(message);
        System.out.println(sendResult);
        System.out.println("==========" + (System.currentTimeMillis() - startTime));
    }
}
