package com.anatorini.lab06.Buoy.Core;

import java.io.IOException;
import java.net.ServerSocket;

public class BuoyServer extends Thread{
    private ServerSocket ss;
    public BuoyServer(ServerSocket ss){
        System.out.println("Buoy server started.");
        this.ss = ss;
    }
    @Override
    public void run(){
        try{
            System.out.println("Buoy server started at "+ss.getLocalSocketAddress());
            System.out.println("Buoy server listening for connections...");
            while(!ss.isClosed()){
                new BuoyClientHandler(ss.accept()).start();
            }
        }catch(IOException ignored){}

    }
}
