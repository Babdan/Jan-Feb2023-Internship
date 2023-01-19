//Server-socket app that reads json from socket and parses it into the console.
//User is able to enter the port to start the server
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class Main {
    public static void main(String[] args) {
        // print out the local IP address
        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
            try {
                // create a server socket
                // prompt the user for the port number
                Scanner input = new Scanner(System.in);
                System.out.print("Enter the port number: ");
                int port = input.nextInt();
                // newlines
                System.out.println();
                ServerSocket server = new ServerSocket(port);
                    Socket socket = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = in.readLine();
                    // parse the JSON and print it to the console
                    JSONObject json = new JSONObject(line);
                    System.out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
