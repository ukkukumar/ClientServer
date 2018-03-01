
import java.io.*;
import java.net.*;
import java.util.*;

class ClientThread extends Thread {
    DatagramSocket sock_instance;
    private int    MAX_DATA_SEGMENT = 1000;
    private int    client_port;
    private        InputStream fileData;
    private        byte[] file_bytes;
    private        String eof_data;
    private        String file_bytes_read;
    private        byte[]  receive_bytes = new byte[1000];
    private        byte[]  send_bytes = new byte[1000];
    private        String  inputFile;
    private        DatagramPacket sendPacket;
    private        DatagramPacket receivePacket;
    private        int total_bytes = 0;

    public ClientThread(DatagramSocket server_socket, String inputFile, DatagramPacket receivePacket) {
        this.sock_instance = server_socket;
        this.inputFile = inputFile;
        this.receivePacket = receivePacket;
    }

    @Override 
    public void run() {
        try {
            //DatagramPacket receivePacket = new DatagramPacket(receive_bytes, 1000);
            try {
                //sock_instance.receive(receivePacket);
            } catch (Exception e) {
                System.out.println("Exception"); 
            }
		
            InetAddress clientIpAddress = receivePacket.getAddress();
            client_port = receivePacket.getPort();
			
            //String send_data = new String("data from server to client");
            //send_bytes = send_data.getBytes();
            //sendPacket = new DatagramPacket(send_bytes, send_bytes.length, 
            //                                clientIpAddress, client_port);
            //serverSocket.send(sendPacket);

            try {
                File file = new File(this.inputFile);
                //file.close();
                String file_header;
                long     file_length = file.length();
                Long     longInstance = new Long(file_length);
                String   length_as_string = longInstance.toString();
                file_header = this.inputFile + " " + "length: " + length_as_string;
                System.out.println("File header: " + file_header);
                //System.out.println("File name: " + this.inputFile + " file length: " + file_length);
                //String send_data = new String("data from server to client");
                send_bytes = file_header.getBytes();
                sendPacket = new DatagramPacket(send_bytes, send_bytes.length, 
                                                clientIpAddress, client_port);
                sock_instance.send(sendPacket);
            } catch (Exception e) {

            }
             
            try {
                fileData = new FileInputStream(this.inputFile);
            } catch (Exception e) {

            }

            long flow_control_check_starttime;
            long per_sec_data_send_starttime;
            long difference;
            long wait_time;
            long wait_time_in_sec;
            long bytes_sent_in_sec = 0;
            long diff_in_millisec;
            long remaining_wait_time;
            byte[] file_bytes = new byte[1000];
            //byte[] file_bytes = new byte[10];

            flow_control_check_starttime = System.nanoTime();
            per_sec_data_send_starttime = System.nanoTime();

            try {

 
                while (fileData.read(file_bytes) != -1) {
                    file_bytes_read = new String(file_bytes);
                    //System.out.println("61: total_bytes: " + total_bytes + " File bytes read: " + file_bytes_read);
                    total_bytes += file_bytes_read.length();
                    bytes_sent_in_sec += file_bytes_read.length();
         
                    sendPacket = new DatagramPacket(file_bytes, file_bytes.length, 
                                                    clientIpAddress, client_port);
                    sock_instance.send(sendPacket);
                    difference = System.nanoTime() - flow_control_check_starttime;
                    diff_in_millisec = difference/1000000;

                    // Check for every 50 ms
                    if (diff_in_millisec >= 50) {
                        //Check if bytes sent is greater than 1 MB since last wait time
                        System.out.println("87: 50 millisec check");
                        if (bytes_sent_in_sec >= 1000000) {
                            System.out.println("89: 1 MB check");
                            wait_time = System.nanoTime() - per_sec_data_send_starttime;
                            // Check if more than 1 MB of data sent in 1-sec
                            //System.out.println("Wait time: " + wait_time);
                            if (wait_time <= (1000000 * 1000)) {
                                System.out.println("94: 1 MB in 1 sec check");
                                // If more than 1 MB data sent, wait for the remaining time in 1-sec to elapse
                                remaining_wait_time = (1000000 * 1000) - wait_time;
                                sleep(remaining_wait_time/1000000);
                                per_sec_data_send_starttime = System.nanoTime();
                                bytes_sent_in_sec = 0;
                            }
                        }
                        flow_control_check_starttime = System.nanoTime();
                    }
                }
            } 
            catch (Exception e) {
          
            }

            try {
                fileData.close();
             } 
             catch (Exception e) {

             }

             eof_data = new String("EOF");
             send_bytes = eof_data.getBytes();
             //System.out.println("78: Sending bytes length: " + send_bytes.length);
             sendPacket = new DatagramPacket(send_bytes, send_bytes.length, 
                                             clientIpAddress, client_port);
             try {
                 sock_instance.send(sendPacket);
             } catch (Exception e) {

             }
        } 
        finally {

        }
    }
}

public class Userver {

    public static void main(String[] args) throws Exception {
        byte[]  receive_bytes = new byte[1000];
        byte[]  send_bytes = new byte[1000];
        DatagramSocket serverSocket = null;
        int count = 0;
        DatagramPacket sendPacket = null;
        int    MAX_DATA_SEGMENT = 1000;
		
        if (args.length < 2) {
            System.out.println("Invalid number of arugments");
        }
		
        //System.out.println("Command line arguments: " + args.length + " " + args[0] + " " + args[1]);
        //System.out.println("port: " + testport);
        //System.out.println("File name: " + args[1]);
        int testport = Integer.parseInt(args[0]);
        String inputFile = args[1];
		
        try {
            serverSocket = new DatagramSocket(testport);
		
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receive_bytes, MAX_DATA_SEGMENT);
                serverSocket.receive(receivePacket);
                String receiveString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(" ");
                System.out.println("printing on server: From Client: " + receiveString);
                System.out.println(" ");

                // Create new thread for each client connection
                Thread t = new ClientThread(serverSocket, inputFile, receivePacket);
                t.start();
            }
        } finally {
            System.out.println("printing on server: server end");
            serverSocket.close();
        }
    }
}

