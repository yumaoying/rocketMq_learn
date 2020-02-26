package com.yu;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;

/**
 * Created by yumaoying
 * 生产者-批量发送(一次只发送不超过1MiB的消息)
 */
public class ProducerBatch {

    public static void main(String[] args) throws Exception {
        //1.创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("my_pGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");
        //启动生产者
        producer.start();
        //如果一次只发送不超过1MiB的消息，则可以轻松使用批处理
        Message msg1 = new Message("myTopic002", "批量消息1".getBytes());
        Message msg2 = new Message("myTopic002", "批量消息2".getBytes());
        Message msg3 = new Message("myTopic002", "批量消息3".getBytes());

        ArrayList<Message> list = new ArrayList<Message>();
        list.add(msg1);
        list.add(msg2);
        list.add(msg3);
        //批量发送消息
        SendResult sendResult3 = producer.send(list);

        System.out.println(sendResult3);
        //关闭
        producer.shutdown();
        System.out.println("已经停机");
    }
}
