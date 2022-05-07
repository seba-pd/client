package client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MemberService {

    final static String ADD_MEMBER_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/add";
    final static String CHECK_IF_MEMBER_EXIST = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/add";
    final static String GET_CHANNELS_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/channels";
    private final Map<String, Thread> activeChannelThreadList = new HashMap<>();

    private final ClientApi clientApi = new ClientApi();

    public boolean addMember(String memberName) {
        var response = clientApi.addMember(memberName, ADD_MEMBER_URL);
        return response.equals("Member already exist");
    }

    public boolean checkIfMemberExist(String memberName) {
        var response = clientApi.checkIfMemberExist(memberName, CHECK_IF_MEMBER_EXIST);
        return response.equals("Member not found");
    }

    public void joinToPreviousChannels(String memberName, ChannelService channelService) {
        var response = clientApi.getChannelListToJoin(memberName, GET_CHANNELS_URL);
        Arrays.stream(response.split(",")).forEach(c -> joinToChannel(memberName, channelService,c));
    }

    public void joinToChannel(String memberName, ChannelService channelService, Scanner scanner) {
        var channelName = getChannel(scanner);
        if (channelService.joinToChannel(channelName, memberName)) {
            var channelListener = new Thread(new JmsService(memberName, channelName));
            activeChannelThreadList.put(channelName, channelListener);
            channelListener.start();
        }
    }

    private void joinToChannel(String memberName, ChannelService channelService, String channelName) {
        if (channelService.joinToChannel(channelName, memberName)) {
            var channelListener = new Thread(new JmsService(memberName, channelName));
            activeChannelThreadList.put(channelName, channelListener);
            channelListener.start();
        }
    }

    public void exitFromChannel(String memberName, ChannelService channelService, Scanner scanner){
        var channelName = getChannel(scanner);
        if (channelService.exitFromChannel(channelName, memberName)){
            var channelToExit = activeChannelThreadList.get(memberName);
            channelToExit.interrupt();
        }
    }

    public String getChannel(Scanner scanner) {
        System.out.println("Enter channel name: ");
        return scanner.nextLine();
    }
}
