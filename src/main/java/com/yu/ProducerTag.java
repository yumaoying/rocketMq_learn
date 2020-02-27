package com.yu;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * Created by yumaoying
 * 生产者-tag过滤
 */
public class ProducerTag {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("my_pGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");
        //启动生产者
        producer.start();
        for (int i = 0; i < 5; i++) {
            //tag是用来过滤的，用于消息分组
            //key主要是用来业务处理，找消息，一般用于关联
            Message message = new Message("myTopic002", "tag-1", "key-1", ("消息" + i).getBytes());
            //单向消息,只发送不保证一定发送成功，失败了消息不会重投，因此有可能会丢失消息
            producer.send(message);
        }
        System.out.println("发送成功");
        //关闭
        producer.shutdown();
        System.out.println("已经停机");
    }
}
