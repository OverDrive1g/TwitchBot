package model;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;

public class IRCBot extends PircBot{
    private OnMessage callback;
    private OnJoin onJoin;
    private String channel;

    public IRCBot() {
        this((sender, message) -> System.out.println("sender = [" + sender + "], message = [" + message + "]"));
    }

    public IRCBot(OnMessage callback) {
        this.setName("overdrive1g");
        this.isConnected();
        this.callback = callback;
//        setVerbose(true);
    }

    public void join(String oAuth, String channel) {
        this.channel = channel;
        try {
            this.connect("irc.chat.twitch.tv", 6667, oAuth);
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
        joinChannel(channel);
    }

    public void setOnJoin(OnJoin onJoin) {
        this.onJoin = onJoin;
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        onJoin.onJoin(channel, sender, login, hostname);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        callback.onMessage(sender, message);
        if (message.equalsIgnoreCase("!vk")) {
            this.sendMessage(channel, String.format("@%s, vk.com/dd.junior", sender));
        }
    }

    public interface OnMessage{
        void onMessage(String sender, String message);
    }

    public interface OnJoin{
        void onJoin(String channel, String sender, String login, String hostname);
    }
}
