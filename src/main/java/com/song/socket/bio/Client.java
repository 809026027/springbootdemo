package com.song.socket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author songfeng
 * @version 1.0
 * @since 2015-10-14
 * @category com.feng.test.longconnection
 *
 */
public class Client
{
    private Socket socket;

    private String ip;

    private int port;

    private String id;

    ObjectOutputStream oos;

    BufferedReader br;

    public Client(String ip, int port,String id)
    {
        try
        {
            this.ip = ip;
            this.port = port;
            this.id = id;
            this.socket = new Socket(ip, port);
            this.socket.setKeepAlive(true);
            oos = new ObjectOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(new heartThread()).start();
            new Thread(new MsgThread()).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendMsg(Object content)
    {
        try
        {
            oos.writeObject(content);
            oos.flush();
        }
        catch (Exception e)
        {
            closeSocket();
        }
    }

    public void closeSocket()
    {
        try
        {
            socket.close();
            oos.close();
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    class heartThread implements Runnable
    {
        @Override
        public void run()
        {
            while(true)
            {
                try
                {
                    Thread.sleep(1000);
                    long time = System.currentTimeMillis();
                    //System.out.println("client send:" + time);
                    sendMsg("Client" + id + " send:" + time);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    class MsgThread implements Runnable
    {
        @Override
        public void run()
        {
            String str = null;
            while(true)
            {
                try
                {
                    if(socket.getInputStream().available() > 0)
                    {
                        while((str = br.readLine()) != null)
                        {
                            System.out.println(str);
                        }
                    }
                }
                catch (IOException e)
                {
                    closeSocket();
                }
            }
        }
    }

    public static void main(String[] args)
    {
        Client client1 = new Client("127.0.0.1", 55555, "1");
        client1.sendMsg(new Pojo("songfeng", 26, new ArrayList<String>()));
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Client client2 = new Client("127.0.0.1", 55555, "2");
    }
}