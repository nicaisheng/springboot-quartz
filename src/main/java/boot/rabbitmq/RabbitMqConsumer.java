package boot.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by nicaisheng on 2018/12/14.
 */
public class RabbitMqConsumer {

    public static void main(String[] args) throws Exception {
        String QUEUE_NAME = "hello";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [y] Received '" + message + "'");
                this.getChannel().basicAck(envelope.getDeliveryTag(), false); //手动ack
            }
        };
        channel.basicConsume(QUEUE_NAME, false, consumer);  //第二个参数设置是否自动ack
    }

}
