// pa2 inspired by https://www.digitalocean.com/community/tutorials/java-socket-programming-server-client Java Socket Programming tutorial
// pa3 inspired by https://www.codespeedy.com/how-to-add-an-image-in-jframe/

// imports ordered alphabetically
import java.awt.Container;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.lang.InterruptedException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

// java socket client implementation for CNT 4731 - Programming Assignment
public class client extends JFrame{
    public static void main(String[] args)
        throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        // get localhost IP address
        InetAddress juliaHost = InetAddress.getLocalHost();

        // create resources for server connection
        Socket juliaSocket = new Socket(juliaHost.getHostName(), 4559);
        ObjectInputStream juliaInput = new ObjectInputStream(juliaSocket.getInputStream());
        ObjectOutputStream juliaOutput = null;

        // read server intro message
        String juliaServerMessage = (String) juliaInput.readObject();
        System.out.println(juliaServerMessage);

        // close object streams
        juliaInput.close();

        // create scanner to get user input
        Scanner juliaScanner = new Scanner(System.in);

        boolean exit = false;

        // create a JFrame to display images
        JFrame frame = new JFrame();

        // set default values for frame
        frame.setTitle("Add Image");
        // terminates default flow layout
        frame.setLayout(null); 
        // terminates program on close button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // sets the position of the frame
        // frame.setBounds(100, 200, 350, 300);

         // gets the content layer
        Container container = frame.getContentPane();
        

        while (!exit) {
            // establish socket connection to server using port 4559 (last 4 digits of Julia
            // Chancey's ufid)
            juliaSocket = new Socket(juliaHost.getHostName(), 4559);

            // write to socket using juliaOutput
            juliaOutput = new ObjectOutputStream(juliaSocket.getOutputStream());

            // receive user input using scanner object
            String userInput = juliaScanner.nextLine();

            juliaOutput.writeObject(userInput);

            // read server response message
            juliaInput = new ObjectInputStream(juliaSocket.getInputStream());
            var juliaServerResponse = juliaInput.readObject();

            JLabel label = new JLabel();

            File juliaJpegFile;
            try {
                juliaJpegFile = (File) juliaServerResponse;
                System.out.println(juliaJpegFile.getName());

                // sets the image to be displayed as an icon
                label.setIcon(new ImageIcon(juliaJpegFile.getName()));

                // gets the size of the image
                Dimension size = label.getPreferredSize();

                // sets the location of the image
                label.setBounds(0, 0, size.width, size.height);
                frame.setBounds(100, 200, size.width, size.height);

                // adds objects to the container and exhibits the frame
                container.add(label);
                frame.setVisible(true);

            } catch (Exception e) {
                juliaServerMessage = (String) juliaServerResponse;
                System.out.println(juliaServerMessage);
            }

            // close object streams
            juliaInput.close();
            juliaOutput.close();

            // have thread sleep for 100 milliseconds
            Thread.sleep(100);

            if (userInput.equals("bye"))
                exit = true;
        }
        // close everything
        juliaSocket.close();
        juliaScanner.close();
    }
}
