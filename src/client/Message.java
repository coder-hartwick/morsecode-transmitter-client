package client;


/**
 *
 *
 * @author Jordan Hartwick
 * Jun 6, 2016
 */
public class Message {


    private final String name, content;


    public Message(String name, String content) {
        this.name = name;
        this.content = content;
    }


    public String getName() {
        return name;
    }


    public String getContent() {
        return content;
    }


    @Override
    public String toString() {
        return name+"\n"+content;
    }



}
