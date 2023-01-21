package com.anatorini.lab06.FleetCommand.Core;

import java.io.IOException;
import java.net.ServerSocket;

public class FleetCommandServer extends Thread{
    private ServerSocket ss;

    @Override
    public void run(){
        while(ss.isBound() && !ss.isClosed()){
            try {
                FleetCommandClientHandler fch = new FleetCommandClientHandler(ss.accept());
                fch.start();
            } catch (IOException e) {
                return;
            }
        }
    }
    public FleetCommandServer(ServerSocket ss){
        super();
        this.ss = ss;
    }
}
