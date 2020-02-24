package com.yu;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;

/**
 * Created by yumaoying
 * 生产者-SQL过滤
 */
public class ProducerSql {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("my_pGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");
        //启动生产者
        producer.start();

        //发送多条消息，
        for (int i = 1; i < 100; i++) {
            Message message = new Message("myTopic003", "tag-1", "key-1", ("消息" + i).getBytes());
            message.putUserProperty("age", String.valueOf(i));
            SendResult sendResult = producer.send(message);
            System.out.println("sendResult" + sendResult);
        }

        producer.shutdown();
        // System.out.println("已经停机");
    }
}
