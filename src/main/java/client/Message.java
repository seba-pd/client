package client;

import lombok.Data;

@Data
public class Message {

    private String content;
    private String memberName;
    private String createTime;
    private String channelName;
}
