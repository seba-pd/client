package client.commons;

import lombok.Data;

@Data
public class File {

    private String fileId;
    private String memberName;
    private String fileName;
    private String content;
    private String channelName;
    private String createTime;
}
