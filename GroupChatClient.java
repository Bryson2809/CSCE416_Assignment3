import java.net.*;
import java.io.*;

class GroupChatClient implements Runnable
{
    private BufferedReader fromUserReader;
    private PrintWriter toSockWriter;

    private String clientName;

    public GroupChatClient(BufferedReader reader, PrintWriter writer, String name)
    {
        fromUserReader = reader;
        toSockWriter = writer;
        setClientName(name);
    }

    public void run()
    {
        try
        {
            while (true)
            {
                String line = fromUserReader.readLine();

                if (line == null)
                    break;
                
                toSockWriter.println(getClientName() + ": " + line);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
        System.exit(0);
    }

    public String getClientName()
    {
        return clientName;
    }

    private void setClientName(String name)
    {
        clientName = name;
    }

    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.out.println("Usage: java GroupChatCleint <host> <por> <name>");
            System.exit(1);
        }

        Socket sock = null;
        try
        {
            sock = new Socket(args[0], Integer.parseInt(args[1]));
            System.out.println(args[2] + " is connected to server at " + args[0] + ":" + args[1]);
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }

        try
        {
            PrintWriter toSockWriter = new PrintWriter(sock.getOutputStream(), true);
            BufferedReader fromUserReader = new BufferedReader(new InputStreamReader(System.in));
            Thread child = new Thread(new GroupChatClient(fromUserReader, toSockWriter, args[2]));
            child.start();
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }

        try
        {
            BufferedReader fromSocketReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            while (true)
            {
                String line = fromSocketReader.readLine();

                if (line == null)
                {
                    System.out.println("*** Server quit");
                    break;
                }
                System.out.println(line);
            }
        }
        catch (Exception e)
            {
                System.out.println(e);
                System.exit(1);
            }
            System.exit(0);
    }
}