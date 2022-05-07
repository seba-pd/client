package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsService implements Runnable{

    private final String memberName;
    private final String channelName;

    public JmsService(String memberName, String channelName) {
        this.memberName = memberName;
        this.channelName = channelName;
    }

    private final ClientUi clientUi = new ClientUi();

    public void listenChannel(String channelName, String memberName) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection con = null;

        try {
            con = factory.createConnection();
            con.setClientID(memberName);
            con.start();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(channelName);

            MessageConsumer consumer = session.createDurableSubscriber(topic, memberName);

            javax.jms.Message message;

            while (true) {
                message = consumer.receive(1000);
                if (message instanceof TextMessage textMessage) {
                    mapTextToMessage(textMessage);
                }
            }
        } catch (JMSException | JsonProcessingException e1) {
            e1.printStackTrace();
        }

    }

    private void mapTextToMessage (TextMessage tm) throws JMSException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var message = mapper.readValue(tm.getText(), Message.class);
        clientUi.printMessage(message);
    }

    @Override
    public void run() {
        listenChannel(channelName,memberName);
    }
}
