package com.anatorini.lab06.Buoy.Core;

import com.anatorini.lab06.Buoy.Buoy;
import com.anatorini.lab06.Util.Pair;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BuoyClientHandler extends Thread{
    private Socket s;
    private BufferedReader br;
    private PrintWriter pw;
    @SneakyThrows
    public BuoyClientHandler(Socket s){
        this.s = s;
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        pw = new PrintWriter(s.getOutputStream());
        //System.out.println("New connection from "+s.getRemoteSocketAddress()+":"+s.getPort());
    }

    @Override
    public void run(){
        try{
           while(!s.isClosed()){
                String line = br.readLine();
                if(line == null){
                    continue;
                }
                String[] args = line.split(";");
               // System.out.println("Received: "+line);
                //SHIP_AT;SHIP_ID;X;Y
                if(args[0].equals("SHIP_AT")){
                    int x = Integer.parseInt(args[2]);
                    int y = Integer.parseInt(args[3]);
                    int id = Integer.parseInt(args[1]);
                    System.out.println("Ship "+id+" at "+x+","+y);
                    if(x == -1 && y==-1)
                        Buoy.ships.remove(id);
                    Buoy.ships.put(id,new Pair(x,y));
                    System.out.println("Calculating new sea level...");
                    Buoy.calculateSeaLevels();
                    s.close();
                }
                else if(args[0].equals("ALIVE")) s.close();
                else System.out.println("Unknown command: "+args[0]);
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        }
}
