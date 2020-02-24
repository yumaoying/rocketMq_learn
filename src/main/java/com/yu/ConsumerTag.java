package com.yu;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * Created by yumaoying
 * 消费者-只接受某个tag的消息
 */
public class ConsumerTag {
    public static void main(String[] args) throws MQClientException {
        String consumerGroup = "xxoocsm-2";
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        //设置消费者namesever的地址
        consumer.setNamesrvAddr("192.168.117.110:9876");
        //设置要消费的topic，和生产者生产数据的topic一致
        //参数一topic，参数2，过滤器: 只消费tag-1的消息
        consumer.subscribe("myTopic002", "tag-1");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt message : list) {
                    System.out.println(consumerGroup + ":" + new String(message.getBody()));
                }
                //默认情况下，这条消息只会被一个customer消费到点对点
                //消息被消费了，broker会修改meassage的状态，ack
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //消费者-消费消息的模式：
        //   MessageModel.BROADCASTING-广播模式
        //  MessageModel.CLUSTERING - 集群模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //启动消费者
        consumer.start();
        System.out.println(consumerGroup + "Consumer start....");
    }
}
