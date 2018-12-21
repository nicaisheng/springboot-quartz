package boot.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by nicaisheng on 2018/12/14.
 */
public class RabbitMqProducer {

    public static void main(String[] args) throws Exception {
        String QUEUE_NAME = "hello";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "hello_exchange";
        String routingKey = "hello_routingKey";
        channel.exchangeDeclare(exchangeName, "direct", true); //定义exchange
        channel.queueBind(QUEUE_NAME, exchangeName, routingKey); //通过routingKey把queue和exchange绑定

        byte[] messageBodyBytes = "Hello, world!".getBytes();
        channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes); //发布消息
    }

}
