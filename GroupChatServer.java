import java.io.*;
import java.net.*;
import java.util.*;

public class GroupChatServer implements Runnable
{
    private socket clientSock;
    private static List<PrintWriter> clientList;

    public GroupChatServer(Socket sock)
    {
        clientSock = sock;
    }

    public static synchronized boolean addClient(PrintWriter toClientWriter)
    {
        return(clientList.add(toClientWriter));
    }

    public static synchronized boolean removeClient()
    {
        return(clientList.remove(toClientWriter));
    }

    public static synchronized void relayMessage(PrintWriter fromClientWriter, String mesg)
    {
        //Iterate through the client list and relay message to each client (but not to the sender)
    }

    public void run()
    {
        //Read from the client and relay to other clients
        try
        {
            //Prepare to read from socket
            
            //Get client name

            //Prepare to write to socket with auto flush on

            //Add this client to the active client list

            //Keep doing unitl EOF
            while (true)
            {
                //Read a line from the client

                //If null, client quit, break loop

                //Else, relay the line to all active clients
            }
            //Done with the client, remove it from the client list
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }

    /*
	 * The group chat server program starts from here.
	 * This main thread accepts new clients and spawns a thread for each client
	 * Each child thread does the stuff under the run() method
	 */
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Usage: java GroupChatServer <server port>");
            System.exit(1);
        }

        try
        {
            //Create server socket with given port

            //Keep accepting/serving cleints
            while (true)
            {
                //Wait to accept another client

                //Spawn a thread to read/relay messages from this client
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }
}