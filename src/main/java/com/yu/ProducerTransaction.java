package com.yu;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Created by yumaoying
 * 生产者-事务消息
 */
public class ProducerTransaction {
    public static void main(String[] args) throws Exception {
        //使用事务TransactionMQProducer
        TransactionMQProducer producer = new TransactionMQProducer("my_pGroup2");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");
        System.out.println("测试事务消息======================");
        //回调方法
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                System.out.println("executeLocalTransaction==========");
                System.out.println("msg:" + new String(message.getBody()));
                //在回调方法里执行本地事务
                // ....一系列方法，
                /**
                 * ------------a----------------
                 * a.提交注册信息
                 * b.写入数据库
                 * c.新用户-》发短信
                 * ------------b---------------
                 * 读取消息
                 * 拿到用户信息发短信
                 */
//                try {
//                    //业务逻辑
//                    return LocalTransactionState.COMMIT_MESSAGE;
//                } catch (Exception e) {
//                    return LocalTransactionState.ROLLBACK_MESSAGE;
//                }
                return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                System.out.println("checkLocalTransaction==========");
                System.out.println("checkLocalTransaction msg:" + new String(messageExt.getBody()));
                //  broker端回调，检查事务(当上面的本地事务执行时间过长，broker端回调检查事务)
                // return LocalTransactionState.COMMIT_MESSAGE;
                //回滚的时候，回滚的是半消息
                // return LocalTransactionState.ROLLBACK_MESSAGE;
                //未知时，回再次调用检查
                return LocalTransactionState.UNKNOW;

            }
        });
        //启动生产者
        producer.start();
        Message message = new Message("myTopic002", "测试事务消息1".getBytes());
        //发送事务消息
        TransactionSendResult result = producer.sendMessageInTransaction(message, null);
        System.out.println("TransactionSendResult" + result);
    }
}
