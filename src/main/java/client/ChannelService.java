package client;

import client.commons.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Scanner;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ChannelService {

    private static final String HISTORY_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/channel/history";
    private static final String ADD_CHANNEL_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/channels/add_channel";
    private static final String SEND_MESSAGE_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/message/send";
    private static final String SEND_FILE_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/file/send_file";
    private static final String RECEIVE_FILE_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/file";
    private static final String JOIN_TO_CHANNEL_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/channel/add_member_to_channel";
    private static final String EXIT_FROM_CHANNEL_URL = "http://localhost:8080/chat1b-1.0-SNAPSHOT/chat/channel/remove_member_from_channel";

    private final ClientUi clientUi ;
    private final ClientApi clientApi ;
    private final Scanner scanner;

    public void sendMessage(String memberName) {
        var channelName = getChannelNameFromConsole();
        System.out.println("Enter text: ");
        String messageContent = scanner.nextLine();
        var response = clientApi.sendMessage(channelName, messageContent, memberName, SEND_MESSAGE_URL);
        clientUi.writeMessage(response);
    }

    public void getHistory( String memberName) {
        var channelName = getChannelNameFromConsole();
        var response = clientApi.getHistory(channelName, memberName, HISTORY_URL);
        clientUi.writeHistory(response);
    }


    public void sendFile( String memberName) {
        var channelName = getChannelNameFromConsole();
        System.out.println("Enter file name: ");
        var fileName = scanner.nextLine();
        System.out.println("Enter file path: ");
        var filePath = scanner.nextLine();
        try {
            var content = Files.readAllBytes(Path.of(filePath));
            clientApi.sendFile(fileName, channelName, content, memberName, SEND_FILE_URL);
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }


    public void receiveFile( String memberName) {
        var channelName = getChannelNameFromConsole();
        System.out.println("Enter file name: ");
        var fileName = scanner.nextLine();
        System.out.println("Enter path to save file: ");
        var filePath = scanner.nextLine();
        var response = clientApi.receiveFile(fileName, memberName, channelName, RECEIVE_FILE_URL);
        saveFile(response, filePath);
    }

    private void saveFile(String response, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            var content = mapper.readValue(response, File.class);
            var contentBytes = Base64.getDecoder().decode(content.getContent());
            var file = new java.io.File(filePath);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(contentBytes);
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JsonProcessingException e) {
            System.out.println("Mapper can't map content file");
        }
    }

    public boolean joinToChannel(String channelName, String memberName) {
        var response = clientApi.joinToChannel(channelName, memberName, JOIN_TO_CHANNEL_URL);
        return clientUi.joinToChannel(response);
    }

    public boolean exitFromChannel(String channelName, String memberName) {
        var response = clientApi.exitFromChannel(channelName,memberName,EXIT_FROM_CHANNEL_URL);
        return clientUi.exitFromChannel(response);
    }

    public void addChannel(String memberName) {
        var channelName = getChannelNameFromConsole();
        var response = clientApi.addChannel(channelName,memberName,ADD_CHANNEL_URL);
        clientUi.addChannel(response);
    }

    public String getChannelNameFromConsole() {
        System.out.println("Enter channel name: ");
        return scanner.nextLine();
    }
}

