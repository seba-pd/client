package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsService {

    private final ClientUi clientUi = new ClientUi();

    public void listenChannel(String channelName) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection con = null;

        try {
            con = factory.createConnection();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(channelName);

            MessageConsumer consumer = session.createConsumer(topic);

            consumer.setMessageListener(msg -> {
                try {
                    if (!(msg instanceof TextMessage tm))
                        throw new RuntimeException("no text message");
                    ObjectMapper mapper = new ObjectMapper();
                    var message = mapper.readValue(tm.getText(), Message.class);
                    clientUi.printMessage(message);
                } catch (JMSException | JsonProcessingException e) {
                    System.err.println("Error reading message");
                }
            });
            con.start();
            Thread.sleep(1000000);
        } catch (JMSException | InterruptedException e1) {
            e1.printStackTrace();
        } finally {
            try {
                assert con != null;
                con.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
