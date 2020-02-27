package com.yu;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * Created by yumaoying
 * 生产者-重试机制
 */
public class ProducerRetry {
    public static void main(String[] args) throws Exception {
        //1.创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("my_pGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");

        System.out.println("当消息发送失败时重试");
        //默认重试超时时间是3000毫秒，即3秒
        producer.setSendMsgTimeout(5000);

        //同步消息发送重试
        //默认重试次数是2次
        producer.setRetryTimesWhenSendFailed(3);
        //异步发送失败重试次数,重试次数是2次
        //producer.setRetryTimesWhenSendAsyncFailed(3);

        //是否向其它broker发送请求，默认false
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);
        //启动生产者
        producer.start();
        Message message = new Message("myTopic002", "重试消息测试".getBytes());
        //发送同步消息
        //（会发生等待，消费者消费了消息，会给broker ack应答，broker若在超时事件内没有收到ack，会将消息重投，可能会被consumer集群的其它消费者消费）
        SendResult sendResult = producer.send(message);
        System.out.println("sendResult" + sendResult);
        //关闭
        producer.shutdown();
        System.out.println("已经停机");
    }
}
