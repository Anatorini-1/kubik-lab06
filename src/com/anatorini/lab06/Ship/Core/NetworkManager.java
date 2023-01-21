package com.anatorini.lab06.Ship.Core;

import com.anatorini.lab06.Ship.Ship;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkManager {
    public static int port;
    public static String host;
    public static int shipId = -1;
    public static Socket socket;
    public static boolean oceanHandshake(String host, int port){
        try{
            if(socket != null){
                socket.close();
            }
        Socket socket = new Socket(host, port);
        System.out.println("Connection successful. Handshake commencing...");
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Sending discover packet...");
        out.println("DISCOVER;SHIP;"+ Ship.x +";"+ Ship.y +";");
        String response = in.readLine();
        String[] args = response.split(";");
        if(args[0].equals("DISCOVER") && args[1].equals("OCEAN")) {
            System.out.println("Ocean discovered at " + args[2] + ":" + args[3]);
            NetworkManager.port = port;
            NetworkManager.host = host;
            response = in.readLine();
            args = response.split(";");

            if(args[0].equals("CONFIG")){
                System.out.println("Ocean configuration received.");
                NetworkManager.shipId = Integer.parseInt(args[1]);
                Ship.id = Integer.parseInt(args[1]);
                System.out.println("Ship ID: "+NetworkManager.shipId);
                System.out.println("Handshake successful. Ready to issue commands.");
                new AliveChecker(socket).start();
                NetworkManager.socket = socket;
                Ship.status = "Operational";
                return true;
            }

        }
        else{
            JOptionPane.showMessageDialog(null, "Handshake failed. Server responded with: " + response);
            socket.close();
        }
    }catch (NumberFormatException e){
        JOptionPane.showMessageDialog(null, "Invalid port number.");
    }
        catch(Exception e){
        System.out.println("Connection failed");
            e.printStackTrace();
    }

        return false;
    }

    public static String sendCommand(String command){
        try{
            if(!socket.isClosed()) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println(command);
                out.flush();
                String response = in.readLine();
                System.out.println("Received: " + response);
                return response;
            }
            else{
                JOptionPane.showMessageDialog(null, "Connection lost.");
                return "ERROR;Connection lost.";
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    private static class AliveChecker extends Thread{
        private PrintWriter out;
        private BufferedReader in;
        private Socket socket;
        public AliveChecker(Socket socket){
            try{
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.socket = socket;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            while(!socket.isClosed()){
                try{
                    Thread.sleep(1000);
                    out.println("ALIVE;SHIP;"+NetworkManager.shipId+";");
                    out.flush();
                    //System.out.println("Sent ALIVE packet.");
                    String response = in.readLine();
                    //System.out.println("Received: " + response);
                    if(response== null) continue;
                    String[] args = response.split(";");
                    if(!args[0].equals("ALIVE")){
                        System.out.println("Ocean is not alive. Exiting...");
                        System.exit(0);
                    }
                }catch (InterruptedException | IOException e){
                    return;
                }
            }
        }
    }
}
