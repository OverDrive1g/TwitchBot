package com.overdrive.bangtwitch.model;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;

public class IRCBot extends PircBot{
    private OnMessage callback;
    private String channel;

    public IRCBot() {
        this.setName("soronzonboldyn_batcjecjeg");
        this.isConnected();
        this.callback = (sender, message) -> {/*todo*/};
        setVerbose(true);
    }

    public IRCBot(OnMessage callback) {
        this.setName("soronzonboldyn_batcjecjeg");
        this.isConnected();
        this.callback = callback;
        setVerbose(true);
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

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.equalsIgnoreCase("!vk")) {
            this.sendMessage(channel, String.format("@%s, vk.com/dd.junior", sender));
        }
    }

    public interface OnMessage{
        void onMessage(String sender, String message);
    }
}
