import java.io.*;
import java.net.*;
import java.util.*;

public class GroupChatServer implements Runnable
{
    private Socket clientSock;
    private static List<PrintWriter> clientList = new ArrayList<PrintWriter>();

    public GroupChatServer(Socket sock)
    {
        clientSock = sock;
    }

    public static synchronized boolean addClient(PrintWriter toClientWriter)
    {
        return (clientList.add(toClientWriter));
    }

    public static synchronized boolean removeClient(PrintWriter toClientWriter)
    {
        return (clientList.remove(toClientWriter));
    }

    public static synchronized void relayMessage(PrintWriter fromClientWriter, String mesg)
    {
        for (PrintWriter p : clientList)
        {
            if (!p.equals(fromClientWriter))
                p.println(mesg);
        }
    }

    public void run()
    {
        try 
        {
            BufferedReader fromSockReader = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            PrintWriter toSockWriter = new PrintWriter(clientSock.getOutputStream(), true);

            addClient(toSockWriter);

            while (true)
            {
                String line = fromSockReader.readLine();
                if (line == null)
                {
                    break;
                }
                relayMessage(toSockWriter, line);
            }
            removeClient(toSockWriter);
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }
    
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Usage: java GroupChatServer <server port>");
            System.exit(0);
        }

        try
        {
            Socket cliSock = null;
            ServerSocket servSock = new ServerSocket(Integer.parseInt(args[0]));

            while (true)
            {
                try 
                {
                    System.out.println("Waiting for a client...");
                    cliSock = servSock.accept();
                    System.out.println("New client connected");

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