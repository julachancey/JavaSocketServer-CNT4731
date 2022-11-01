// inspired by https://www.digitalocean.com/community/tutorials/java-socket-programming-server-client Java Socket Programming tutorial

// imports ordered alphabetically
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.lang.InterruptedException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

// java socket client implementation for CNT 4731 - Programming Assignment 2
public class client {
    public static void main(String[] args)
            throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        // get localhost IP address
        InetAddress juliaHost = InetAddress.getLocalHost();

        // create resources for server connection
        Socket juliaSocket = null;
        ObjectInputStream juliaInput = null;
        ObjectOutputStream juliaOutput = null;

        // create scanner to get user input
        Scanner juliaScanner = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {
            // establish socket connection to server using port 4559 (last 4 digits of Julia
            // Chancey's ufid)
            juliaSocket = new Socket(juliaHost.getHostName(), 4559);

            // write to socket using juliaOutput
            juliaOutput = new ObjectOutputStream(juliaSocket.getOutputStream());
            System.out.println("Sending request to Socket Server");

            // receive user input using scanner object
            String userInput = juliaScanner.nextLine();

            juliaOutput.writeObject(userInput);

            // read server response message
            juliaInput = new ObjectInputStream(juliaSocket.getInputStream());

            String juliaServerMessage = (String) juliaInput.readObject();

            System.out.println("Message from server: " + juliaServerMessage);

            // close object streams
            juliaInput.close();
            juliaOutput.close();

            // have thread sleep for 100 milliseconds
            Thread.sleep(100);

            if (userInput.equals("bye"))
                exit = true;
        }

        juliaScanner.close();
    }
}
