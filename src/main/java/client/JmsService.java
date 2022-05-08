package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class JmsService extends Thread{


    private  final String channelName;
    private final Connection connection;
    @Setter
    private ClientUi clientUi = new ClientUi();
    @Setter
    private boolean isStop = false;

    public JmsService( String channelName,Connection connection) {

        this.channelName = channelName;
        this.connection = connection;
    }


    public void listenChannel(String channelName,Connection con) {
        try {
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(channelName);

            MessageConsumer consumer = session.createConsumer(topic);

            javax.jms.Message message;

            while (true) {
                message = consumer.receive();
                if (message instanceof TextMessage textMessage) {
                    mapTextToMessage(textMessage);
                    if (isStop) {
                        consumer.close();
                        break;
                    }
                }
            }
            session.close();
        } catch (JMSException | JsonProcessingException e1) {
            e1.printStackTrace();
        }
    }

    private void mapTextToMessage (TextMessage tm) throws JMSException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var message = mapper.readValue(tm.getText(), client.commons.Message.class);
        clientUi.printMessage(message);
    }

    @Override
    public void run() {
        listenChannel(channelName,connection);
    }
}
