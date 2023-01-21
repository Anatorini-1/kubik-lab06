package com.anatorini.lab06.FleetCommand.Core;

import com.anatorini.lab06.FleetCommand.FleetCommand;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Data
public class NetworkManager {
    private int oceanPort;
    private String oceanHost;
    private String oceanStatus = "DISCONNECTED";
    private String fleetCommandStatus = "DISCONNECTED";
    private int fleetCommandPort;
    private String fleetCommandHost;
    private ServerSocket serverSocket;

    public NetworkManager() {
        oceanPort = 0;
        oceanHost = "";
        fleetCommandPort = 0;
        fleetCommandHost = "";
        serverSocket = null;
    }

    public boolean oceanHandshake(String host, int port) {
        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress(host, port));
            s.getOutputStream().write(("DISCOVER;FLEET;" + s.getLocalAddress().getHostAddress()+"\n").getBytes());
            s.getOutputStream().flush();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            line = br.readLine();
            System.out.println("Received: " + line);
            String[] args = line.split(";");
            if(args[0].equals("DISCOVER") && args[1].equals("OCEAN")){
                oceanHost = args[2];
                oceanPort = port;
                fleetCommandPort = Integer.parseInt(args[3]);
                fleetCommandHost = s.getLocalAddress().getHostAddress();
                line = br.readLine();
                args = line.split(";");
                if(args[0].equals("CONFIG") && args[1].equals("SIZE")){
                    FleetCommand.oceanWidth = Integer.parseInt(args[2]);
                    FleetCommand.oceanHeight = Integer.parseInt(args[3]);
                    FleetCommand.fleetBuoyModels = new FleetBuoyModel[FleetCommand.oceanWidth][FleetCommand.oceanHeight];
                }
                s.close();
                if(serverSocket != null && !serverSocket.isClosed()){
                    serverSocket.close();
                }
                serverSocket = new ServerSocket(fleetCommandPort);
                new FleetCommandServer(serverSocket).start();
                oceanStatus = "CONNECTED";
                fleetCommandStatus = "CONNECTED";
                new AliveChecker().start();
                return true;
            }
            else{
                s.close();
                return false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBuoyCount(){
        int res = 0;
        if(FleetCommand.fleetBuoyModels == null) return res;
        for(int i=0;i<FleetCommand.fleetBuoyModels.length;i++){
            for(int j=0;j<FleetCommand.fleetBuoyModels[i].length;j++){
                if(FleetCommand.fleetBuoyModels[i][j] != null) res++;
            }
        }
        return res;
    }

    private class AliveChecker extends Thread{
        private int timeout = 0;
        public void run(){
            while(serverSocket != null && !serverSocket.isClosed()){
                if(timeout>5){
                    oceanStatus = "DISCONNECTED";
                    fleetCommandStatus = "DISCONNECTED";
                    oceanHost= "";
                    oceanPort = 0;
                    fleetCommandHost = "";
                    fleetCommandPort = 0;
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        System.out.println("Connection to Ocean lost.");
                        System.out.println("Server closed.");
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Socket s = new Socket();
                    s.connect(new InetSocketAddress(oceanHost, oceanPort));
                    s.getOutputStream().write(("ALIVE;FLEET;"+fleetCommandHost+";"+fleetCommandPort+"\n").getBytes());
                    s.getOutputStream().flush();
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    line = br.readLine();
                    if(line == null) continue;
                    String[] args = line.split(";");
                    if(args[0].equals("ALIVE") && args[1].equals("OCEAN")){
                        timeout = 0;
                    }
                    s.close();
                } catch (IOException e) {
                    System.out.println("Connection to Ocean lost.");
                    FleetCommand.networkManager.oceanPort = 0;
                    FleetCommand.networkManager.oceanHost = null;
                    try {
                        serverSocket.close();
                    } catch (IOException ignored) {
                        System.out.println("Server closed.");
                    }
                    serverSocket = null;
                }
                timeout++;
            }
        }
    }
}
