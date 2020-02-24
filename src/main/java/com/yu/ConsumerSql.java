package com.yu;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * Created by yumaoying
 * 消费者
 */
public class ConsumerSql {
    public static void main(String[] args) throws MQClientException {
        String consumerGroup = "xxoocsm";
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        //设置消费者namesever的地址
        consumer.setNamesrvAddr("192.168.117.110:9876");

        //消费者将收到tagA或tagb的消息，但是限制是一条消息只能由一条标签，
        //而对于这种复杂的情况可能无效，在这种情况下，可以使用SQL表达式过滤消息
        //配置，需要在 broker.conf 文件设置添加 enablePropertyFilter=true的配置
        //然后在启动broker时加载指定配置文件  ../bin/mqbroker -n 10 192.168.117.110:9876 -c broker.conf
        MessageSelector messageSelector = MessageSelector.bySql("age>=18 and age<=28");
        consumer.subscribe("myTopic003", messageSelector);

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt message : list) {
                    System.out.println(consumerGroup + "," + new String(message.getBody()));
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
