package com.channelsoft.socket;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by yuanshun on 2014/12/17.
 */
public class Client {
    public static void main(String[] arg) throws IOException {

        for (int i = 0; i < 10; i++) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket s = null;
                    try {
                        s = new Socket("127.0.0.1", 8848);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(s.isClosed());
                    System.out.println(s.isConnected());
                    DataOutputStream dos = null;
                    DataInputStream dis = null;
                    try {
                        dos = new DataOutputStream(s.getOutputStream());
                        dis = new DataInputStream(s.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        new DataInputStream(s.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        while (true) {
                            String name = JOptionPane.showInputDialog("請輸入信息:");
                            dos.writeUTF("SLEEKNETGEOCK4stsjeSonline:" + name);
                            String readinfo = dis.readUTF();
                            System.out.println("接收服务端信息:" + readinfo);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }).run();
        }


    }
}
