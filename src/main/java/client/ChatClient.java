package client;

import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        ChannelService channelService = new ChannelService();
        MemberService memberService = new MemberService();

        Scanner scanner = new Scanner(System.in);
        var memberName = chatClient.login(scanner, channelService,memberService);

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("/exit chat")) break;
            switch (input) {
                case "/s" -> channelService.sendMessage(scanner, memberName);
                case "/h" -> channelService.getHistory(scanner, memberName);
                case "/sf" -> channelService.sendFile(scanner, memberName);
                case "/rf" -> channelService.receiveFile(scanner, memberName);
                case "/ac" -> channelService.addChannel(scanner, memberName);
                case "/rc" -> memberService.exitFromChannel(memberName,channelService,scanner);
                case "/jc" -> memberService.joinToChannel(memberName,channelService,scanner);
                default -> System.out.println("Wrong command");
            }
        }
    }

    private String login(Scanner scanner, ChannelService channelService, MemberService memberService) {
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
            memberService.joinToPreviousChannels(memberName,channelService);
        }
        System.out.println("Welcome");
        return memberName;
    }
}
