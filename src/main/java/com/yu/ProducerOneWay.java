package com.yu;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * Created by yumaoying
 * 生产者-单项消息发送
 */
public class ProducerOneWay {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("my_pGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");
        //启动生产者
        producer.start();
        Message message = new Message("myTopic002", "消息3".getBytes());
        //单向消息,只发送不保证一定发送成功，失败了消息不会重投，因此有可能会丢失消息
        producer.sendOneway(message);
        System.out.println("发送成功");
        //关闭
        producer.shutdown();
        System.out.println("已经停机");
    }
}
