import java.net.*;
import java.io.*;

class GroupChatClient implements Runnable
{
    //Read from keybaord
    private BufferedReader fromUserReader;
    //Writing to socket
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
                //Read line from user
                String line = fromUserReader.readLine();

                //Check for null and end if null
                if (line == null)
                    break;
                
                    //Write lines to socket
                    toSockWriter.println(getClientName() + ": " + line);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }

        //End the other thread
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
        //Check argument is correct
        if (args.length != 3)
        {
            System.out.println("Usage: java GroupChatCleint <host> <por> <name>");
            System.exit(1);
        }

        //Connect to server at host and port #
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

        //Thread to read from user and write to socket
        try
        {
            //Prep to write to socket with autoflush
            PrintWriter toSockWriter = new PrintWriter(sock.getOutputStream(), true);
            //Prepare to read from keyboard
            BufferedReader fromUserReader = new BufferedReader(new InputStreamReader(System.in));
            //Spawn thread to read from user and write to socket
            Thread child = new Thread(new GroupChatClient(fromUserReader, toSockWriter, args[2]));
            child.start();
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }

        //Read from socket and display message to user
        try
        {
            //Prep to read from socket
            BufferedReader fromSocketReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            //Keep looping until server is finished
            while (true)
            {
                //Read line from socket
                String line = fromSocketReader.readLine();

                //If null EOF
                if (line == null)
                {
                    //Tell user to quit
                    System.out.println("*** Server quit");
                    break;
                }

                //Write line to user
                System.out.println(line);
            }
        }
        catch (Exception e)
            {
                System.out.println(e);
                System.exit(1);
            }

            //End other thread 
            System.exit(0);
    }
}