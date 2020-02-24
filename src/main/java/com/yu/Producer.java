package com.yu;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;

/**
 * Created by yumaoying
 * 生产者-同步消息发送
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("my_pGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");
        //启动生产者
        producer.start();

        Message message = new Message("myTopic002", "消息1".getBytes());

        //发送多条消息，
        ArrayList<Message> arrayList = new ArrayList<>();
        arrayList.add(message);
        //发送同步消息
        //（会发生等待，消费者消费了消息，会给broker ack应答，broker若在超时事件内没有收到ack，会将消息重投，可能会被consumer集群的其它消费者消费）
        SendResult sendResult = producer.send(message);
        System.out.println("sendResult" + sendResult);
        //关闭

        // System.out.println("已经停机");
    }
}
