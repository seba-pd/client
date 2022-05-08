package client;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.util.Scanner;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ChatClient {

    private final ChannelService channelService;
    private final MemberService memberService;
    private final Scanner scanner;
    private Connection connection;

    public void start() {

        var memberName = login();

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("/exit chat")) break;
            switch (input) {
                case "/s" -> channelService.sendMessage(memberName);
                case "/h" -> channelService.getHistory(memberName);
                case "/sf" -> channelService.sendFile(memberName);
                case "/rf" -> channelService.receiveFile(memberName);
                case "/ac" -> channelService.addChannel(memberName);
                case "/rc" -> memberService.exitFromChannel(memberName);
                case "/jc" -> memberService.joinToChannel(memberName,channelService.getChannelNameFromConsole(),connection);
                default -> System.out.println("Wrong command");
            }
            }
        try {
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private Connection createBrokerConnection(String memberName) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection con = null;
        try {
            con = factory.createConnection();
            con.setClientID(memberName);
            con.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return con;
    }

    private String login() {
        System.out.println("Do you want create new member \"n\" ?");
        String memberName;
        var isNew = scanner.nextLine();
        if (isNew.equals("n")) {
            System.out.println("Enter name : ");
            while (memberService.addMember(memberName = scanner.nextLine())) {
                System.out.println("Member name is already occupied");
            }
            connection = createBrokerConnection(memberName);
        } else {
            System.out.println("Enter name : ");
            while (memberService.checkIfMemberExist(memberName = scanner.nextLine())) {
                System.out.println("Member not found");
            }
            connection = createBrokerConnection(memberName);
            memberService.joinToPreviousChannels(memberName,connection);
        }
        System.out.println("Welcome");
        return memberName;
    }
}
