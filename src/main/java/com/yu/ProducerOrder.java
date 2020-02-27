package com.yu;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yumaoying
 * 顺序写入消息
 * 需要producer在发消息时保证数据发送时在同一个queue，同一个topic里
 * 使用同一个线程发消息是，发送顺序和写入broker顺序一致
 */
public class ProducerOrder {
    public static void main(String[] args) throws Exception {
        //1.创建生产者，使用DefaultMQProducer发消息，是一个线程
        DefaultMQProducer producer = new DefaultMQProducer("my_pGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");
        //启动生产者
        producer.start();
        System.out.println("顺序发送.............");
        for (int i = 0; i < 10; i++) {
            //顺序消费时，发送的topic一致
            Message msg = new Message("myTopic002", ("顺序消息" + i).getBytes());
            //send()方法中MessageQueueSelector用于指定消息队列，queue选择器，向topic中的哪个queue去写消息
            producer.send(msg, new MessageQueueSelector() {
                //设置发送的queue是同一个

                /***
                 *
                 * @param mqs 队列
                 * @param msg 发送的消息
                 * @param arg 参数与send方法的arg对应
                 * @return
                 */
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer arg1 = (Integer) arg;
                    MessageQueue messageQueue = mqs.get(arg1);
                    return messageQueue;
                }
            }, 1);
            System.out.println(Thread.currentThread() + ":发送消息，" + msg);
        }
        //关闭
        producer.shutdown();
    }
}
