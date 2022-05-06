package client;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClient {

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        ChannelService channelService = new ChannelService();
        MemberService memberService = new MemberService();
        JmsService jmsService = new JmsService();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Scanner scanner = new Scanner(System.in);
        var memberName = chatClient.login(scanner, memberService, executor);

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("/exit chat")) break;
            switch (input) {
                case "/s" -> channelService.sendMessage(scanner, memberName);
                case "/h" -> channelService.getHistory(scanner, memberName);
                case "/sf" -> channelService.sendFile(scanner, memberName);
                case "/rf" -> channelService.receiveFile(scanner, memberName);
                case "/rc" -> channelService.exitFromChannel(scanner, memberName);
                case "/ac" -> channelService.addChannel(scanner, memberName);
                case "/jc" -> {
                    System.out.println("Enter channel name: ");
                    var channelName = scanner.nextLine();
                    if (channelService.joinToChannel(channelName, memberName)) {
                        executor.execute(new Thread(() -> jmsService.listenChannel(channelName)));
                    }
                }
                default -> System.out.println("Wrong command");
            }
        }
    }

    private String login(Scanner scanner, MemberService memberService, ExecutorService executor) {
        System.out.println("Do you want create new member \"n\" ?");
        String memberName;
        var isNew = scanner.nextLine();
        if (isNew.equals("n")) {
            System.out.println("Enter name : ");
            while (memberService.addMember(memberName = scanner.nextLine())) {
                System.out.println("Member name is already occupied");
            }
        } else {
            System.out.println("Enter name : ");
            while (!memberService.checkIfMemberExist(memberName = scanner.nextLine())) {
                System.out.println("Member not found");
            }
            memberService.joinToPreviousChannels(memberName, executor);
        }
        System.out.println("Welcome");
        return memberName;
    }
}
