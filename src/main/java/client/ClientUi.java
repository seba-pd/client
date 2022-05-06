package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.util.Arrays;

@Log
public class ClientUi {

    @SneakyThrows
    public void writeHistory(String response){
        if(errorCatch(response)) return;
        ObjectMapper mapper = new ObjectMapper();
        var messageList = Arrays.asList(mapper.readValue(response, Message[].class));
        messageList.forEach(this::printMessage);
    }

    public boolean joinToChannel(String response){
        if(errorCatch(response)) return false;
        System.out.println("You joined to channel");
        return true;
    }

    public void exitFromChannel(String response) {
        if(errorCatch(response)) return;
        System.out.println("You left from channel");
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
}
