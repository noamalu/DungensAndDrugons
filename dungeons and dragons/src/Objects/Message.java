package Objects;

import Game.MessageCallback;

public class Message {
    private String message;
    private MessageCallback receiver;
    public Message(String message){
        System.out.println(message);
    }
}
