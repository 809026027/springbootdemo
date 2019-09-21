package com.song.socket.bio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author songfeng
 * @version 1.0
 * @since 2015-10-14
 * @category com.feng.test.longconnection
 *
 */
public class Server
{
    private ServerSocket serverSocket;

    private Socket socket;

    private int port;

    static List<Socket> list = new ArrayList<Socket>();

    ExecutorService exec;

    public Server(int port)
    {
        try
        {
            this.port = port;
            this.serverSocket = new ServerSocket(port);

            //线程池管理客户端线程。
            exec = Executors.newCachedThreadPool();
            while (true)
            {
                socket = serverSocket.accept();
                list.add(socket);
                exec.execute(new MsgThread(socket));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void closeSocket()
    {
        try
        {
            for(Socket s : list)
            {
                s.close();
            }
            serverSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    class MsgThread implements Runnable
    {
        private Socket socket;

        private long lastHeatTime = System.currentTimeMillis();

        public MsgThread(Socket socket)
        {
            this.socket = socket;
        }

        @Override
        public void run()
        {
            ObjectInputStream oin = null;
            PrintWriter pw = null;
            String str = null;
            try
            {
                oin = new ObjectInputStream(this.socket.getInputStream());
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())));
                while(true)
                {
                    if(!socket.isConnected())
                    {
                        break;
                    }
                    if(socket.getInputStream().available() > 0)
                    {
                        if(System.currentTimeMillis() - lastHeatTime > 5000)
                        {
                            break;
                        }

                        oin.readFields();
                        Object object = oin.readObject();
                        if(object instanceof String)
                        {
                            lastHeatTime = System.currentTimeMillis();
                            long time = System.currentTimeMillis();
                            pw.println(object + ",Server back:" + time);
                            pw.flush();
                        }
                        else if(object instanceof Pojo)
                        {
                            pw.println(object + ",Server back:" + ((Pojo)object).getName());
                            pw.flush();
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    this.socket.close();
                    list.remove(socket);
                    if(oin != null)
                    {
                        oin.close();
                    }
                    pw.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args)
    {
        Server server = new Server(55555);
    }
}