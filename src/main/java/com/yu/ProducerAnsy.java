package com.yu;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;

/**
 * Created by yumaoying
 * 生产者-异步发送消息
 */
public class ProducerAnsy {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("my_pGroup");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.117.110:9876");
        //启动生产者
        producer.start();

        Message message = new Message("myTopic002", "消息2".getBytes());
        //1.异步可靠消息
        //不会阻塞，等待broker的确定;而是采用事件监听方式接受broker返回的确定
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("消息发送成功...");
                System.out.println("sendResult" + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                //如果发生异常，尝试重投，或者调用业务逻辑处理
                throwable.printStackTrace();
                System.out.println("发生异常");
            }
        });

        //2.异步单向消息,只发送不保证一定发送成功，失败了消息不会重投，因此有可能会丢失消息
        producer.sendOneway(message);
        //关闭
//        producer.shutdown();
        System.out.println("已经停机");
    }
}
