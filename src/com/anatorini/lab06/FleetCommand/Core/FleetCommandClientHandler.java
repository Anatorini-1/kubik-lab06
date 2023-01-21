package com.anatorini.lab06.FleetCommand.Core;

import com.anatorini.lab06.FleetCommand.FleetCommand;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FleetCommandClientHandler extends Thread{
    private final Socket socket;
    private final PrintWriter pw;
    private final BufferedReader br;
    private BuoyAliveChecker checker;
    @SneakyThrows
    public FleetCommandClientHandler(Socket accept) {
        super();
        this.socket = accept;
        pw = new PrintWriter(socket.getOutputStream());
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    @Override
    public void run(){
        String line;
        System.out.println("New connection from "+socket.getRemoteSocketAddress()+":"+socket.getPort());
        while(!socket.isClosed()){
            //System.out.println("Waiting for input...");
            try{
                line = br.readLine();
                if (line == null) continue;
                //System.out.println("Recived "+line+" from "+socket.getRemoteSocketAddress());
                String[] args = line.split(";");
                if(!args[0].equals("UPLINK")) System.out.println("Recived "+line+" from "+socket.getRemoteSocketAddress());
                    switch (args[0]){
                        case "DISCOVER" -> {
                            pw.write("DISCOVER;FLEET_COMMAND;"+socket.getLocalAddress().getHostAddress()+";"+socket.getPort()+"\n");
                            pw.flush();
                        }
                        case "REGISTER" -> {
                            if ("BUOY".equals(args[1])) {//REGISTER;BUOY;X;Y;HOST;PORT;
                                if (FleetCommand.fleetBuoyModels == null) {
                                    pw.write("ERROR;FLEET_NOT_CONFIGURED\n");
                                } else {
                                    int x = Integer.parseInt(args[2]);
                                    int y = Integer.parseInt(args[3]);
                                    FleetCommand.fleetBuoyModels[x][y] = new FleetBuoyModel(x, y, socket);
                                    System.out.println("Registered buoy at " + x + "," + y);
                                    checker = new BuoyAliveChecker(x,y);
                                    checker.start();
                                    pw.write("ACK\n");
                                    pw.flush();
                                }
                            }
                        }
                        case "ERROR" -> {
                            System.out.println(line);
                        }
                        case "UPLINK" -> {
                            //UPLINK;BUOY;X;Y;1,0,1,1,2, .... ,1 // [5][5] int array
                            if(!args[1].equals("BUOY")) pw.write("ERROR,UNEXPECTED_REQUEST");
                            int x = Integer.parseInt(args[2]);
                            int y = Integer.parseInt(args[3]);
                            if(FleetCommand.fleetBuoyModels == null) pw.write("ERROR;FLEET_NOT_CONFIGURED\n");
                            else if(FleetCommand.fleetBuoyModels[x][y] == null) pw.write("ERROR;UNEXPECTED_INPUT");
                            else{
                                String[] data = args[4].split(",");
                                    //data.size = 25
                                int[][] buoyData = new int[5][5];
                                for(int i=0;i<5;i++){
                                    for(int j=0;j<5;j++){
                                        //System.out.println("data["+i*5+j+"] = "+data[i*5+j]);
                                        buoyData[i][j] = Integer.parseInt(data[i*5+j]);
                                    }
                                }
                                FleetCommand.fleetBuoyModels[x][y].setData(buoyData);
                                pw.write("ACK\n");
                                checker.timeout = 0;
                                pw.flush();
                            }
                        }
                    }

Thread.sleep(100);
            }catch (IOException | InterruptedException ignored){

            }
        }
    }
    private class BuoyAliveChecker extends Thread{
        public int timeout = 0;
        private int x;
        private int y;
        public BuoyAliveChecker(int x,int y){
            super();
            this.x = x;
            this.y = y;

        }

        @Override
        public void run() {
            while(!socket.isClosed()){
                if(timeout > 2){
                    System.out.println("Buoy at "+socket.getRemoteSocketAddress()+" timed out");
                    FleetCommand.fleetBuoyModels[x][y] = null;
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                    timeout++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
