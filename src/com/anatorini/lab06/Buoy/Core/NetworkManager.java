package com.anatorini.lab06.Buoy.Core;

import com.anatorini.lab06.Buoy.Buoy;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class NetworkManager {
    public static String oceanAddress;
    public static String buoyAddress;
    public static String fleetCommandHost;
    public static int oceanPort;
    public static int buoyPort;
    public static int fleetCommandPort;
    public static ServerSocket ss;
    public static boolean oceanHandshake(String host, int port){
        Socket s = new Socket();
        try{
            s.connect(new InetSocketAddress(host,port));
            s.getOutputStream().write(("DISCOVER;BUOY;"+s.getLocalAddress().getHostAddress()+"\n").getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println("Discover packet sent to ocean. Await response...");
            String line = br.readLine();
            System.out.println("Received: "+line);
            String[] args = line.split(";");
            if(args[0].equals("DISCOVER") && args[1].equals("OCEAN")){
                oceanAddress = args[2];
                oceanPort = port;
                buoyAddress = s.getLocalAddress().getHostAddress();
                buoyPort = Integer.parseInt(args[3]);
            }else return false;
            line = br.readLine();
            System.out.println("Received: "+line);
            args = line.split(";");
            if(args[0].equals("CONFIG") && args[1].equals("POSITION")){
                Buoy.x = Integer.parseInt(args[2]);
                Buoy.y = Integer.parseInt(args[3]);
            }else return false;
            line = br.readLine();
            System.out.println("Received: "+line);
            args = line.split(";");
            if(args[0].equals("CONFIG") && args[1].equals("FLEET_COMMAND")){
                if(!args[2].equals("OFFLINE")) {
                    fleetCommandHost = args[2];
                    fleetCommandPort = Integer.parseInt(args[3]);
                }
            }
        }catch(IOException e){
            return false;
        }finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ocean handshake complete.");
        System.out.println("Ocean at "+oceanAddress+":"+oceanPort);
        System.out.println("FleetCommand at "+(fleetCommandHost != null ?  fleetCommandHost+":"+fleetCommandPort : "OFFLINE"));
        return true;
    }/*
    public static boolean fleetCommandHandshake(){
        System.out.println("Initializing Fleet Command handshake at "+fleetCommandHost+":"+fleetCommandPort+"...");
        if(fleetCommandHost == null) return false;
        Socket s = new Socket();
        try{
            s.connect(new InetSocketAddress(fleetCommandHost,fleetCommandPort));
            System.out.println("Connected to Fleet Command at "+s.getRemoteSocketAddress());
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println("Sending discover packet...");
            pw.write("DISCOVER;BUOY");
            pw.flush();
            String line = br.readLine();
            System.out.println("Received "+line);
            String[] args = line.split(";");
            if (!args[0].equals("DISCOVER") || !args[1].equals("FLEET_COMMAND")) {
                return false;
            }
            System.out.println("Sending config packet...");
            pw.write("REGISTER;BUOY;"+Buoy.x+";"+Buoy.y+";"+buoyAddress+";"+buoyPort);

            line = br.readLine();
            System.out.println("Received "+line);
            args = line.split(";");
            if(args[0].equals("ACK")) {
                System.out.println("Fleet Command handshake complete.");
                new FleetCommandConnection(s).start();
                System.out.println("Registered to FleetCommand at "+fleetCommandHost+":"+fleetCommandPort+" position: ["+Buoy.x+"] [" +Buoy.y+"]");
                return true;
            }
            else {
                System.out.println(line);
                return false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */
    @SneakyThrows
    public static boolean fleetCommandHandshake(){
        System.out.println("Initializing Fleet Command handshake at "+fleetCommandHost+":"+fleetCommandPort+"...");
        String host = fleetCommandHost;
        int port = fleetCommandPort;
        if(fleetCommandHost == null) return false;
        Socket s = new Socket();
            s.connect(new InetSocketAddress(host,port));
            s.getOutputStream().write(("DISCOVER;BUOY;"+s.getLocalAddress().getHostAddress()+"\n").getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println("Discover packet sent to Fleet Command. Await response...");
            String line = br.readLine();
            System.out.println("Received: "+line);
            String[] args = line.split(";");
            if(args[0].equals("DISCOVER") && args[1].equals("FLEET_COMMAND")){
                oceanAddress = args[2];
                oceanPort = port;
                buoyAddress = s.getLocalAddress().getHostAddress();
                buoyPort = Integer.parseInt(args[3]);
            }else return false;
            s.getOutputStream().write(("REGISTER;BUOY;"+Buoy.x+";"+Buoy.y+";"+buoyAddress+";"+buoyPort+"\n").getBytes());
            System.out.println("Register packet sent to Fleet Command. Await response...");
            line = br.readLine();
            System.out.println("Received: "+line);
            args = line.split(";");
            if(args[0].equals("ACK")) {
                System.out.println("Fleet Command handshake complete.");
                new FleetCommandConnection(s).start();
                System.out.println("Registered to FleetCommand at " + fleetCommandHost + ":" + fleetCommandPort + " position: [" + Buoy.x + "] [" + Buoy.y + "]");

                return true;
            }
            else {
                System.out.println(line);
                return false;
            }
    }

    public static boolean startBuoyServer() throws IOException {
        System.out.println("Starting Buoy server at "+buoyAddress+":"+buoyPort);

            ss = new ServerSocket(buoyPort);
            new BuoyServer(ss).start();

        System.out.println("Buoy server started at "+ss.getInetAddress()+":"+ss.getLocalPort());
        return true;
    }
}
