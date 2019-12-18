package rabbitwhataver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@localhost");
        factory.setConnectionTimeout(300000);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare("best-queue", true, false, false, null);

            DeliverCallback callback = new OnReceiveCallback();
            channel.basicConsume("best-queue", false, callback, consumerTag -> {});

            while (true) { }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
