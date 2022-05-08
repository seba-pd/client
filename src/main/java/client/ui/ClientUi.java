package client.ui;

import client.commons.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;

import java.util.Arrays;

@Singleton
public class ClientUi {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void writeHistory(String response){
        if(errorCatch(response)) return;
        var messageList = Arrays.asList(mapper.readValue(response, Message[].class));
        messageList.forEach(this::printMessage);
    }

    public boolean joinToChannel(String response){
        if(errorCatch(response)) return false;
        System.out.println("You joined to channel");
        return true;
    }

    public boolean exitFromChannel(String response) {
        if(errorCatch(response)) return false;
        System.out.println("You left from channel");
        return true;
    }

    public void addChannel(String response){
        if(errorCatch(response)) return;
        System.out.println("You create a channel");
    }

    @SneakyThrows
    public void writeMessage(String response){
        if(errorCatch(response)) return;
        System.out.println("Message was sent");
    }

    private boolean errorCatch(String response){
        var errorCheck = true;
        switch(response){
            case "Channel already exist" -> System.out.println("Channel already exist");
            case "Channel not found" -> System.out.println("Wrong channel name");
            case "Member not exist in channel" -> System.out.println("Member not exist in channel");
            case "Member already exist in channel" -> System.out.println("Member already exist in channel");
            case "File not found" -> System.out.println("File not found");
            case "Member not found" -> System.out.println("Member not found");
            case "Message not found" -> System.out.println("Message not found");
            default -> errorCheck = false;
        }
        return errorCheck;
    }

    public void printMessage(Message message) {
        System.out.println(message.getCreateTime() + " : " + message.getChannelName() + " : " +
                message.getMemberName() + " : " + message.getContent());
    }

    public void showOption() {
        System.out.println("/s - send a file to channel");
        System.out.println("/h - get channel history");
        System.out.println("/sf - send file to channel");
        System.out.println("/rf - receive file from channel");
        System.out.println("/ac - add channel");
        System.out.println("/ec - exit from channel");
        System.out.println("/jc - join to channel channel");
        System.out.println();
    }
}
