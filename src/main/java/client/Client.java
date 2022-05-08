package client;


import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;


public class Client {

    public static void main(String[] args) {

        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        var chatClient = container.select(ChatClient.class).get();
        chatClient.start();
    }
}
