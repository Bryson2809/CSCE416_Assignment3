import java.io.*;
import java.net.*;
import java.util.*;

public class GroupChatServer implements Runnable
{
    private Socket clientSock;

    private static List<PrintWriter> clientList;

    public GroupChatServer(Socket sock)
    {
        clientSock = sock;
    }

    public static synchronized boolean addClient(PrintWriter toClientWriter)
    {
        return clientList.add(toClientWriter);
    }

    public static synchronized boolean removeClient(PrintWriter toClientWriter)
    {
        return clientList.remove(toClientWriter);
    }

    public static synchronized void relayMessage(PrintWriter fromClientWriter, String mesg)
    {
        for (PrintWriter p : clientList)
        {
            if (p.equals(fromClientWriter));
            else    p.println(mesg);
        }
    }

    public void run()
    {
        try 
        {
            BufferedReader fromSockReader = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));


        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }
    
    public static void main(String[] args)
    {
        //Giver server a port to listen to
        if (args.length != 1)
        {
            System.out.println("Usage: java GroupChatServer <server port>");
            System.exit(0);
        }

        try
        {
            Client cliSock = null;

            while (true)
            {
                try 
                {
                    ServerSocket servSock = new ServerSocket(Integer.parseInt(args[0]));
                    cliSock = servSock.accept();
                    System.out.println("Client connected");

                    Thread child = new Thread(new GroupChatServer(cliSock));
                    child.start();
                }
                catch (Exception e)
                {
                    System.out.println(e);
                    System.exit(1);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }
}