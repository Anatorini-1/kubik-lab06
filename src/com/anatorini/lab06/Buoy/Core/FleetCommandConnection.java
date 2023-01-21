package com.anatorini.lab06.Buoy.Core;

import com.anatorini.lab06.Buoy.Buoy;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FleetCommandConnection extends Thread{
    private Socket s;
    private BufferedReader br;
    private PrintWriter pw;
    @SneakyThrows
    public FleetCommandConnection(Socket s){
        this.s = s;
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        pw = new PrintWriter(s.getOutputStream());
    }

    @Override
    public void run(){
        String line;
        try{
            while(!s.isClosed()){
                Thread.sleep(100);
                String serialized = "";
                if(Buoy.seaLevels == null) continue;
                for(int i=0;i<Buoy.seaLevels.length;i++)
                    for(int j=0;j<Buoy.seaLevels[i].length;j++){
                        serialized = serialized.concat(Buoy.seaLevels[i][j] + ",");
                    }
                //System.out.println("Uplinking to Fleet Command at "+s.getRemoteSocketAddress()+":"+s.getPort()+"...");
                s.getOutputStream().write(("UPLINK;BUOY;"+Buoy.x+";"+Buoy.y+";"+serialized+"\n").getBytes());;
                String res = br.readLine();
                if(res == null) continue;
                String[] args = res.split(";");
                switch (args[0]){
                    case "ACK" -> {
                        //System.out.println("Uplink successful.");
                        continue;
                    }
                    case "ERROR" -> {
                        switch (args[1]){
                            case "UNEXPECTED_REQUEST" -> {}
                            case "UNAUTHORIZED" ->{}
                            case "FLEET_NOT_CONFIGURED" -> {}
                        }
                    }
                }
            }
        }
        catch (InterruptedException | IOException e) {return;}
    }
}
