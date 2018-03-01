
import java.net.*;  
import java.io.*; 

public class Uclient {
    public static void main(String[] args) throws Exception {
	
        //String sendString = new String("data from client to server");
        String sendString = new String("start");
        byte[] send_bytes = new byte[1000];
        byte[] receive_bytes = new byte[1000];
        int count = 0;
        long total_bytes = 0;
		
        DatagramSocket client_socket = new DatagramSocket();

        if (args.length < 2) {
            System.out.println("Invalid number of arugments");
        }
		
        System.out.println("Command line arguments: " + args.length + " " + args[0] + " " + args[1]);
        //InetAddress testaddress = InetAddress.getByName(args[0]);
        InetAddress myIPaddress = InetAddress.getByName(args[0]);
        int server_port = Integer.parseInt(args[1]);
        //int         server_port = 50002;
        //System.out.println("port: " + testport);
		
        try {
            //while (true) {
                send_bytes = sendString.getBytes();
		
                DatagramPacket sendPacket = new DatagramPacket(send_bytes, sendString.length(), 
                                                               myIPaddress, server_port);
                client_socket.send(sendPacket);

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receive_bytes, receive_bytes.length);
                client_socket.receive(receivePacket);
                System.out.println("receive_bytes.length: " + receivePacket.getLength());
	
                String receiveString = "";  
                //receiveString.setText("");              	
                receiveString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                total_bytes += receivePacket.getLength();
                System.out.println("total bytes: " + total_bytes + " printing on client: From server: " + receiveString);
               
                if (receiveString.equals("EOF")) {
                    System.out.println("EOF received ");
                    break;
                }
            }
        } finally {
            client_socket.close();
        }
    }
}
