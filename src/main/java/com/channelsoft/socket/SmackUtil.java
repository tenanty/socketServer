package com.channelsoft.socket;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * 通过smack链接Openfire
 * Created by yuanshun on 2014/12/22.
 */
public class SmackUtil {

    public static void main(String[] args) {
        ConnectionConfiguration configuration = new ConnectionConfiguration("10.130.24.220", 5222, "");
        configuration.setSASLAuthenticationEnabled(false);
        try {
            XMPPConnection connection = new XMPPConnection(configuration);
            connection.connect();
            connection.login("y1", "y1");//登陆
            System.out.println(connection.getUser());
            ChatManager chatManager = connection.getChatManager();

            Chat newChat = chatManager.createChat("test3@pc20141212", new MessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    System.out.println("Received from [" + message.getFrom() + "] message:" + message.getBody());
                }
            });

            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean b) {
                    chat.addMessageListener(new MessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {
                            System.out.println("Received From [" + message + "] message:" + message.getBody());
                        }
                    });
                }
            });

            newChat.sendMessage("我是张三");

            //获取花名册
            Roster roster = connection.getRoster();
            Collection<RosterEntry> entries = roster.getEntries();
            for (RosterEntry entry : entries) {
                System.out.println(entry.getName() + "-" + entry.getUser() + "-" + entry.getType() + "-" + entry.getGroups().size());
                Presence presence = roster.getPresence(entry.getUser());
                System.out.println("-" + presence.getStatus() + "-" + presence.getFrom());
            }

            //添加花名册监听器,监听好友状态的改变
            roster.addRosterListener(new RosterListener() {
                @Override
                public void entriesAdded(Collection<String> collection) {
                    System.out.println("entriesAdded");
                }

                @Override
                public void entriesUpdated(Collection<String> collection) {
                    System.out.println("entriesUpdated");
                }

                @Override
                public void entriesDeleted(Collection<String> collection) {
                    System.out.println("entriesDeleted");
                }

                @Override
                public void presenceChanged(Presence presence) {
                    System.out.println("presenceChanged ->" + presence.getStatus());
                }
            });

            //创建组
            for (RosterGroup g : roster.getGroups()) {
                for (RosterEntry entry : g.getEntries()) {
                    System.out.println("Group:" + g.getName() + ">>" + entry.getName() + "-" + entry.getUser() + "-" + entry.getType() + "-" + entry.getGroups().size());
                }
            }

            //发送消息
            BufferedReader cmdIn = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                try {
                    String cmd = cmdIn.readLine();
                    if ("!q".equals(cmd)) {
                        break;
                    }
                    newChat.sendMessage(cmd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
