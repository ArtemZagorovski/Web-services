package rabbitwhataver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import rabbitwhataver.OnReceiveCallback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class Consumer implements Runnable{
    private String url;
    private String queue;
    public Consumer(String url, String queue){
        this.url = url;
    this.queue = queue;
    }

    @Override
    public void run() {
        try{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(url);
        factory.setConnectionTimeout(300000);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queue, true, false, false, null);

            DeliverCallback callback = new OnReceiveCallback();
            channel.basicConsume(queue, false, callback, consumerTag -> {});

            while (true) { }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
} catch (Exception e){
    System.out.println("Ooops");
}
    }
}
