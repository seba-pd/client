package client;


import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;


public class ClientApplication {

    public static void main(String[] args) {

        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        var chatClient = container.select(Client.class).get();
        chatClient.start();
    }
}
