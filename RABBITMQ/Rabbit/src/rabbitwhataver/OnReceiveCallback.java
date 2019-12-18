package rabbitwhataver;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OnReceiveCallback implements DeliverCallback {
    private final static String LAMBDA_URL = "https://k6tr0s1oa6.execute-api.us-east-1.amazonaws.com/dev/process";
    private final static String FROM = "a.zagorovski23@gmail.com";
    private final static String TO = "artemzagorovski@mail.ru";
    private final static String SUBJECT = "Forward tasks";
    private final static String SMTP_HOST = "email-smtp.us-west-2.amazonaws.com";
    private final static String SMTP_USERNAME = "AKIAW2CC2ZAENI2NRPGM";
    private final static String SMTP_PASSWORD = "BK2389We97jvL09dmx5eRnG8kU1oSL2F2Ojkjn1++Gm6";

    @Override
    public void handle(String s, Delivery delivery) throws IOException {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        System.out.println("Message consumed: " + message);
        String processedMessage = getProcessedMessage(message).replaceAll("^\"(.*)\"$", "$1");
        sendEmail(processedMessage);
    }

    private String getProcessedMessage(String message) throws IOException {
        HttpPost post = new HttpPost(LAMBDA_URL);
        StringEntity myEntity = new StringEntity(message, ContentType.create("text/plain", "UTF-8"));
        post.setEntity(myEntity);
        String result;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)){
            result = EntityUtils.toString(response.getEntity());
        }

        return result;
    }

    private void sendEmail(String text) {
        Email email = EmailBuilder.startingBlank()
                .from(FROM)
                .to(TO)
                .withSubject(SUBJECT)
                .withHTMLText(text)
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer(SMTP_HOST, 587, SMTP_USERNAME, SMTP_PASSWORD)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(10 * 1000)
                .clearEmailAddressCriteria()
                .withDebugLogging(true)
                .buildMailer();

        mailer.sendMail(email);
    }
}
