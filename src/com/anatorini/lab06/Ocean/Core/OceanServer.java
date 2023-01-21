package com.anatorini.lab06.Ocean.Core;

import java.io.IOException;
import java.net.ServerSocket;

public class OceanServer extends Thread{
    private ServerSocket ss;

    @Override
    public void run(){
        try {
            while(!ss.isClosed()){
                new OceanClientHandler(ss.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("Server closed.");
        }
    }
    public OceanServer(ServerSocket ss){
        super();
        this.ss = ss;
    }
}
