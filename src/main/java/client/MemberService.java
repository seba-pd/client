package client;

public class MemberService {

    final static String ADD_MEMBER_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/add";
    final static String CHECK_IF_MEMBER_EXIST = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/member/add";
    private final ClientApi clientApi = new ClientApi();

    public boolean addMember(String memberName) {
        var response = clientApi.addMember(memberName, ADD_MEMBER_URL);
        return response.equals("Member already exist");
    }

    public boolean checkIfMemberExist(String memberName) {
        var response = clientApi.checkIfMemberExist(memberName, CHECK_IF_MEMBER_EXIST);
        return response.equals("Member not found");
    }

}
