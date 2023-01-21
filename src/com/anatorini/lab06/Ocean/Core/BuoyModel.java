package com.anatorini.lab06.Ocean.Core;
import com.anatorini.lab06.Ocean.Ocean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Data @AllArgsConstructor
public class BuoyModel {
    private int x;
    private int y;
    private int port;
    private String host;
    private int timeout = 5;

    @SneakyThrows
    public void setFleetCommand(String host, int port){
        Socket s = new Socket();
        s.connect(new InetSocketAddress(host, port));
        s.getOutputStream().write(("CONFIG;FLEET_COMMAND; " + host + ";"+port+"\n").getBytes());
        s.getOutputStream().flush();
        s.close();
    }
    public void notify(String message){
        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress(host, port));
            s.getOutputStream().write((message+"\n").getBytes());
            s.getOutputStream().flush();
            s.close();
        } catch (IOException e) {
            Ocean.buoys[x][y] = null;
        }
       // System.out.println("Buoy " + x + "," + y + " notified: " + message);
    }
}
