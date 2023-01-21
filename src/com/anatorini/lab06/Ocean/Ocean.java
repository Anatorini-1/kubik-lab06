package com.anatorini.lab06.Ocean;

import com.anatorini.lab06.Ocean.Core.BuoyModel;
import com.anatorini.lab06.Ocean.Core.OceanServer;
import com.anatorini.lab06.Ocean.Core.ShipModel;
import com.anatorini.lab06.Ocean.GUI.OceanFrame;
import lombok.Synchronized;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Ocean {
    public static int port;
    public static String host;
    public static int oceanWidth;
    public static int oceanHeight;
    public  static BuoyModel[][] buoys;
    @Synchronized
    public  static BuoyModel addBuoy(Socket s) {
        int i=0,j=0;
       // System.out.println(1);
            outer:
            for (i = 0; i < Ocean.buoys.length; i++) {
                for (j = 0; j < Ocean.buoys[i].length; j++) {
                    if (buoys[i][j] == null) {
                        //System.out.println(2);
                        buoys[i][j] = new BuoyModel(i, j, s.getPort(), s.getInetAddress().getHostAddress(),5);
                        //System.out.println(3);
                        return buoys[i][j];
                    }
                }
        }
        return null;
    }

    public static HashMap<Integer,ShipModel> ships;
    public static int shipCount =0;
    public static ServerSocket serverSocket;

    public static String fleetCommandHost;
    public static int fleetCommandPort = 0;
    public static int fleetCommandTimeout = 0;

    private static OceanFrame oceanFrame;
    public static void main(String[] args){
        System.out.println("Ocean Starting...");
        System.out.println("Initializing GUI...");
        oceanFrame = new OceanFrame();
        ships = new HashMap<>();
        System.out.println("GUI Initialized.");
        System.out.println("Ocean Started. Awaiting network initialization.");
    }

    public static void initNetwork(int port, int size){
        System.out.println("Initializing Network...");
        System.out.println("Port: " + port);
        System.out.println("Size: " + size);
        Ocean.port = port;
        Ocean.oceanWidth = size;
        Ocean.oceanHeight = size;
        buoys = new BuoyModel[size][size];
        if(serverSocket!=null && serverSocket.isBound()){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            serverSocket = new ServerSocket(port);
            new OceanServer(serverSocket).start();
        }
        catch (IOException | RuntimeException e){
            System.out.println("Error initializing network.");
            JOptionPane.showMessageDialog(oceanFrame, "Error initializing network. Potential port conflict.");
        }

        System.out.println("Network Initialized.");
        Ocean.startChecker();
        System.out.println("Ocean Ready. Awaiting Clients...");
    }
    public static void setFleetCommand(String host,int port){
        System.out.println("FleetCommand discovered at " + host + ":" + port);
        fleetCommandHost = host;
        fleetCommandPort = port;
        for(int i=0;i< buoys.length;i++){
            for(int j=0;j<buoys[i].length;j++){
               if(buoys[i][j] != null) buoys[i][j].setFleetCommand(fleetCommandHost,fleetCommandPort);
            }
        }
    }

    public static void notifyBuoys(int x,int y,int id){
        ShipModel ship = ships.get(id);
        if(buoys == null) return;
        int shipX = x/5;
        int shipY = y/5;
        int rangeStartX =shipX > 2 ? shipX-2 : 0;
        int rangeEndX = shipX < buoys.length-2 ? shipX+2 : buoys.length-1;
        int rangeStartY =shipY > 2 ? shipY-2 : 0;
        int rangeEndY = shipY < buoys[0].length-2 ? shipY+2 : buoys[0].length-1;
        for(int i=rangeStartX;i<=rangeEndX;i++){
            for(int j=rangeStartY;j<=rangeEndY;j++){
                if(buoys[i][j] != null) buoys[i][j].notify("SHIP_AT;"+id+";" + ship.getX() + ";" + ship.getY() + "\n");
            }
        }
       // System.out.println("Notified buoys [" + rangeStartX + "," + rangeEndX + "]x[" + rangeStartY + "," + rangeEndY + "]");
    }

    public static void startChecker(){
        System.out.println("Starting checker...");
        new BuoyChecker().start();
    }

    private static class BuoyChecker extends  Thread{
        @Override
        public void run(){
            while(true) {
                try {
                    Thread.sleep(1000);
                    for (int i = 0; i < buoys.length; i++) {
                        for (int j = 0; j < buoys[i].length; j++) {
                            if (buoys[i][j] != null){
                                if(buoys[i][j].getTimeout() > 0) buoys[i][j].setTimeout(buoys[i][j].getTimeout()-1);
                                else buoys[i][j].notify("ALIVE\n");
                            }
                            //System.out.println("Notified buoy " + i + "," + j);
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
