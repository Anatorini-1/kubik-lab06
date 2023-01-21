package com.anatorini.lab06.Ocean.Core;

import com.anatorini.lab06.Ocean.Ocean;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

public class OceanClientHandler extends Thread{
    private final Socket s;
    private final BufferedReader br;
    private final PrintWriter pw;
    private int timeout;
    @SneakyThrows
    public OceanClientHandler(Socket s){
        this.s = s;
        //System.out.println("Connected to "+s.getInetAddress()+" on port "+s.getPort());
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
        timeout = 0;
    }
    @Override
    public void run(){
        while (!s.isClosed()){
            try{
                String line = br.readLine();
                if(line==null) continue;
                else{
                    timeout = 0;

                    String[] args = line.split(";");
                    if(!args[0].equals("ALIVE"))System.out.println("Received: "+line);
                    switch (args[0]) {
                        case "DISCOVER" -> {
                            pw.write("DISCOVER;OCEAN;" + s.getLocalAddress().getHostAddress() + ";" + s.getPort() + "\n");
                            pw.flush();
                            switch (args[1]) {
                                case "FLEET" -> {
                                    /*DISCOVER;FLEET_COMMAND;HOST*/
                                    Ocean.setFleetCommand(args[2], s.getPort());
                                    pw.write("CONFIG;SIZE;" + Ocean.oceanWidth +";"+ Ocean.oceanHeight + "\n");
                                    pw.flush();
                                    s.close();
                                    new FleetAliveChecker().start();
                                    return;
                                }
                                case "SHIP" -> {
                                    //DISCOVER;SHIP;X;Y;
                                    ShipModel ship =new ShipModel(Integer.parseInt(args[2]),Integer.parseInt(args[3]),"OK",0,++Ocean.shipCount);
                                    Ocean.ships.put(Ocean.shipCount,ship);
                                    Ocean.notifyBuoys(ship.getX(), ship.getY(), ship.getId());
                                    pw.write("CONFIG;"+Ocean.shipCount+"\n");
                                    pw.flush();
                                    new ShipAliveChecker(ship,s).start();
                                }
                                case "BUOY" -> {
                                    //DISCOVER;BUOY;HOST
                                    BuoyModel buoy = Ocean.addBuoy(s);
                                    if(buoy == null){
                                        pw.write("ERROR;OCEAN;FULL\n");
                                    }
                                    else {
                                        pw.write("CONFIG;POSITION;" + buoy.getX() + ";" + buoy.getY() + "\n");
                                        System.out.println("Buoy " + buoy.getX() + "," + buoy.getY() + " added");
                                        if (Ocean.fleetCommandHost != null && Ocean.fleetCommandPort != 0) {
                                            pw.write("CONFIG;FLEET_COMMAND;" + Ocean.fleetCommandHost + ";" + Ocean.fleetCommandPort + "\n");
                                            //System.out.println("Sent config to buoy");
                                            //System.out.println("Fleet command host: "+Ocean.fleetCommandHost+" port: "+Ocean.fleetCommandPort);
                                        } else pw.write("CONFIG;FLEET_COMMAND;OFFLINE\n");

                                    }
                                    pw.flush();
                                    s.close();
                                }
                            }
                        }
                        case "MOVE" -> {
                            //MOVE;SHIP;ID;DIRECTION
                            ShipModel ship = Ocean.ships.get(Integer.parseInt(args[2]));
                            ship.setTimeout(0);
                            int targetX = ship.getX();
                            int targetY = ship.getY();
                            switch (args[3]){
                                case "N" -> targetY--;
                                case "S" -> targetY++;
                                case "E" -> targetX++;
                                case "W" -> targetX--;
                                case "NE" -> {
                                    targetX++;
                                    targetY--;
                                }
                                case "NW" -> {
                                    targetX--;
                                    targetY--;
                                }
                                case "SE" -> {
                                    targetX++;
                                    targetY++;
                                }
                                case "SW" -> {
                                    targetX--;
                                    targetY++;
                                }

                            }
                            for(Integer i : Ocean.ships.keySet()){
                                ShipModel sm = Ocean.ships.get(i);
                                if(sm == null) continue;
                                if(sm.getX() == targetX && sm.getY() == targetY){
                                    pw.write("ERROR;COLLISION\n");
                                    pw.flush();
                                    ship.setState("SUNKEN");
                                    Ocean.notifyBuoys(-1, -1, ship.getId());
                                    Ocean.notifyBuoys(-1, -1, sm.getId());
                                    sm.setState("SUNKEN");
                                    return;
                                }
                            }
                            if(ship.getState().equals("SUNKEN")){
                                System.out.println("Replying with: ERROR;SUNKEN");
                                pw.write("ERROR;SUNKEN\n");
                                pw.flush();
                                Ocean.notifyBuoys(-1, -1, ship.getId());
                                //s.close();
                                return;
                            }
                            else if(targetX < 0 || targetX >= Ocean.oceanWidth*5 || targetY < 0 || targetY >= Ocean.oceanHeight*5){
                                System.out.println("Replying with: ERROR;OUT_OF_BOUNDS");
                                pw.write("ERROR;OUT_OF_BOUNDS\n");
                                pw.flush();
                            }
                            else{
                                System.out.println("Replying with: OK");
                                pw.write("OK;"+targetX+";"+targetY+"\n");
                                pw.flush();
                                ship.setX(targetX);
                                ship.setY(targetY);
                            }
                            Ocean.notifyBuoys(ship.getX(),ship.getY(),Integer.parseInt(args[2]));
                        }
                        case "SCAN" -> {
                            //SCAN;SHIP;ID
                            String serialzied = "";
                            for(Integer i : Ocean.ships.keySet()){
                                ShipModel sm = Ocean.ships.get(i);
                                if(sm == null || sm.getId() == Integer.parseInt(args[2])) continue;
                                serialzied = serialzied.concat(""+sm.getX()+","+sm.getY()+":");
                            }
                            pw.write("SCAN_RESULT;"+serialzied+"\n");
                            pw.flush();
                        }
                        case "ABORT" -> {}
                        case "ALIVE" -> {
                            pw.write("ALIVE;OCEAN\n");pw.flush();
                            if(args.length>1 && args[1].equals("FLEET")){
                                Ocean.fleetCommandTimeout = 0;
                                Ocean.fleetCommandHost = s.getInetAddress().getHostAddress();
                                Ocean.fleetCommandPort = Integer.parseInt(args[3]);
                                s.close();
                                return;
                            }
                            if(args.length>1 && args[1].equals("SHIP")){
                                ShipModel ship = Ocean.ships.get(Integer.parseInt(args[2]));
                                ship.setTimeout(0);
                            }
                        }
                    }

                }
                //Thread.sleep(100);
            }catch(IOException e){
            }
        }
        }

        private static class FleetAliveChecker extends Thread{
            @Override
            public void run() {
                while(true){
                    if(Ocean.fleetCommandTimeout>5){
                        Ocean.fleetCommandHost = null;
                        Ocean.fleetCommandPort = 0;
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Ocean.fleetCommandTimeout++;
                }
            }
        }
        private static class ShipAliveChecker extends Thread{
            private ShipModel ship;
            private Socket s;
            public ShipAliveChecker(ShipModel ship,Socket s){
                this.ship = ship;
                this.s = s;
            }
            @Override
            public void run() {
                while(true){
                    //System.out.println("Checking if ship "+ship.getId()+" is alive. Current timeout: "+ship.getTimeout());
                    if(ship.getTimeout() > 2){
                        int x = ship.getX();
                        int y = ship.getY();
                        ship.setX(-1);
                        ship.setY(-1);
                        Ocean.notifyBuoys(x,y,ship.getId());
                        Ocean.ships.remove(ship.getId());
                        try {
                            s.close();
                        } catch (IOException ignored) {

                        }
                        System.out.println("Ship "+ship.getId()+" has been removed");
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                        ship.setTimeout(ship.getTimeout()+1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
