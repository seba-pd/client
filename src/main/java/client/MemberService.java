package client;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

public class MemberService {

    final static String ADD_MEMBER_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/add";
    final static String CHECK_IF_MEMBER_EXIST = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/add";
    final static String GET_CHANNELS_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/channels";

    private final ClientApi clientApi = new ClientApi();
    private final JmsService jmsService = new JmsService();

    public boolean addMember(String memberName) {
        var response = clientApi.addMember(memberName, ADD_MEMBER_URL);
        return response.equals("Member already exist");
    }

    public boolean checkIfMemberExist(String memberName) {
        var response = clientApi.checkIfMemberExist(memberName, CHECK_IF_MEMBER_EXIST);
        return response.equals("Member not found");
    }

    public void joinToPreviousChannels(String memberName, ExecutorService executor) {
        var response = clientApi.getChannelListToJoin(memberName, GET_CHANNELS_URL);
        Arrays.stream(response.split(",")).forEach(c -> executor.execute(new Thread(() -> jmsService.listenChannel(c))));
    }
}
