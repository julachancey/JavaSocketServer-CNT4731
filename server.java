// inspired by https://www.digitalocean.com/community/tutorials/java-socket-programming-server-client Java Socket Programming tutorial

// imports ordered alphabetically
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

// java socket server implementation for CNT 4731 - Programming Assignment 2
public class server {

    // socket server global veriables, port number is last 4 digits of my ufid
    // (6592-4559)
    private static ServerSocket juliaServer;
    private static int juliaPort = 4559;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // create java socket server on port 4559
        juliaServer = new ServerSocket(juliaPort);

        System.out.println("Hello!");

        // server continues running until user types the command "bye"
        while (true) {
            System.out.println("Waiting for the client request");
            // create socket and wait for client connection
            Socket juliaSocket = juliaServer.accept();

            // read from socket into juliaInput
            ObjectInputStream juliaInput = new ObjectInputStream(juliaSocket.getInputStream());

            // convert juliaInput that is a input stream object into a string
            String juliaClientMessage = (String) juliaInput.readObject();

            System.out.println("Message received: " + juliaClientMessage);

            // create output stream object and write juliaClientMessage to juliaSocket
            ObjectOutputStream juliaOutput = new ObjectOutputStream(juliaSocket.getOutputStream());

            juliaOutput.writeObject(juliaClientMessage);

            // close everything
            juliaSocket.close();
            juliaInput.close();
            juliaOutput.close();

            // if user sends "bye" command, terminate the server and exit
            if (juliaClientMessage.equalsIgnoreCase("bye"))
                break;
        }

        System.out.println("Disconnecting");

        // close and terminate julia's server socket
        juliaServer.close();
    }
}