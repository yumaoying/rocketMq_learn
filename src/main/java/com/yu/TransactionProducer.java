package com.yu;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Created by yumaoying
 */
public class TransactionProducer {
    public static void main(String[] args) throws Exception {
        TransactionMQProducer tproducer = new TransactionMQProducer();
        tproducer.setNamesrvAddr("192.168.117.110:9876");
        tproducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                System.out.println("=============executeLocalTransaction");
                System.out.println("e-msg:" + new String(message.getBody()));
                System.out.println("e-msg:" + message.getTransactionId());
                return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt message) {
                System.out.println("=============checkLocalTransaction");
                System.out.println("check-msg:" + new String(message.getBody()));
                System.out.println("check-msg-msg:" + message.getTransactionId());
                return LocalTransactionState.UNKNOW;
            }
        });

        tproducer.start();
        tproducer.send(new Message("testTopic-01","事务消息01".getBytes()));


    }
}
