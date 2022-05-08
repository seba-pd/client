package client;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import javax.jms.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MemberService {

    final static String ADD_MEMBER_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/add";
    final static String CHECK_IF_MEMBER_EXIST = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/add";
    final static String GET_CHANNELS_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/channels";

    private final Map<String, JmsService> activeChannelThreadList = new HashMap<>();
    private final ClientApi clientApi ;
    private final ChannelService channelService;

    public boolean addMember(String memberName) {
        var response = clientApi.addMember(memberName, ADD_MEMBER_URL);
        return response.equals("Member already exist");
    }

    void joinToChannel(String memberName, String channelName, Connection connection) {
        if(channelService.joinToChannel(channelName,memberName)) {
            var channelListener = new JmsService(channelName, connection);
            channelListener.setDaemon(true);
            activeChannelThreadList.put(channelName, channelListener);
            channelListener.start();
        }
    }

    public void exitFromChannel(String memberName){
        var channelName = channelService.getChannelNameFromConsole();
        if (channelService.exitFromChannel(channelName, memberName)){
            var channelToExit = activeChannelThreadList.get(channelName);
            channelToExit.setStop(true);
            activeChannelThreadList.remove(channelName);
        }
    }

    public void joinToPreviousChannels(String memberName,Connection connection) {
        var response = clientApi.getChannelListToJoin(memberName, GET_CHANNELS_URL);
        Arrays.stream(response.split(",")).forEach(c -> joinToPreviousChannel(c,connection));
    }

    public boolean checkIfMemberExist(String memberName) {
        var response = clientApi.checkIfMemberExist(memberName, CHECK_IF_MEMBER_EXIST);
        return response.equals("Member not found");
    }

    private void joinToPreviousChannel(String channelName, Connection connection){
        var channelListener = new JmsService(channelName, connection);
        channelListener.setDaemon(true);
        activeChannelThreadList.put(channelName, channelListener);
        channelListener.start();
    }
}
