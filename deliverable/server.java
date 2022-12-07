// inspired by https://www.digitalocean.com/community/tutorials/java-socket-programming-server-client Java Socket Programming tutorial

// imports ordered alphabetically
import java.io.File;
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

        // create socket and wait for client connection
        Socket juliaSocket = juliaServer.accept();

        // create output stream object and write "Hello!" to juliaSocket
        ObjectOutputStream juliaOutput = new ObjectOutputStream(juliaSocket.getOutputStream());
        juliaOutput.writeObject("Hello!");
        System.out.println("Hello!");

        // close everything
        juliaSocket.close();
        juliaOutput.close();

        // image files to send to client
        File juliaJpegImage1 = new File("image1.jpg");
        File juliaJpegImage2 = new File("image2.jpg");
        File juliaJpegImage3 = new File("image3.jpg");

        // server continues running until user types the command "bye"
        while (true) {
            // create socket and wait for client connection
            juliaSocket = juliaServer.accept();

            // read from socket into juliaInput
            ObjectInputStream juliaInput = new ObjectInputStream(juliaSocket.getInputStream());

            // convert juliaInput that is a input stream object into a string
            String juliaClientMessage = (String) juliaInput.readObject();

            System.out.println(juliaClientMessage);

            // get output stream object
            juliaOutput = new ObjectOutputStream(juliaSocket.getOutputStream());

            // switch statements for client message
            switch (juliaClientMessage) {
                case "JPEG 1":
                    juliaOutput.writeObject(juliaJpegImage1);
                    break;
                case "JPEG 2":
                    juliaOutput.writeObject(juliaJpegImage2);
                    break;
                case "JPEG 3":
                    juliaOutput.writeObject(juliaJpegImage3);
                    break;
                case "bye":
                    // send disconnected message to client to indicate termination and exit
                    juliaOutput.writeObject("disconnected");
                    break;
                default:
                    juliaOutput.writeObject("Error! Command not found.");
                    break;
            }

            // close everything
            juliaSocket.close();
            juliaInput.close();
            juliaOutput.close();

            // if user sends "bye" command, terminate the server and exit
            if (juliaClientMessage.equals("bye"))
                break;
        }

        // close and terminate julia's server socket
        juliaServer.close();
    }
}