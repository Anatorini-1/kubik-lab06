package com.anatorini.lab06.Buoy;

import com.anatorini.lab06.Buoy.Core.NetworkManager;
import com.anatorini.lab06.Util.Pair;

import java.io.IOException;
import java.util.*;

public class Buoy {
    public static int[][] seaLevels;
    public static HashMap<Integer,Pair> ships = new HashMap<>();
    public static int x;
    public static int y;
    public static void  main(String[] args) throws IOException {
        String host;
        int port;
        seaLevels = new int[5][5];
        Arrays.stream(seaLevels).forEach(a -> Arrays.fill(a, 0));
        switch(args.length){
            case 0 -> {host = "localhost"; port = 6000;}
            case 1 -> {host = "localhost"; port = Integer.parseInt(args[0]);}
            case 2 -> {host = args[0]; port=Integer.parseInt((args[1]));}
            default -> {throw new RuntimeException("Unexpected argument");}
        }
        if(!NetworkManager.oceanHandshake(host,port)) throw new RuntimeException("Ocean handshake failed.");
        if(!NetworkManager.startBuoyServer()) throw new RuntimeException("Failed to start buoy server");
        if(!NetworkManager.fleetCommandHandshake()) throw new RuntimeException("FleetCommand handshake failed.");

    }
    public static void calculateSeaLevels(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                seaLevels[i][j] = 0;
            }
        }
        System.out.println("Known ships: " + ships.toString());
        Set<Integer> toDrop = new HashSet<>();
        for (int key : ships.keySet()) {
            Pair ship = ships.get(key);
            boolean hasImpact = false;
            if(ship.getX() != -1){
            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                     int dx = Math.abs(ship.getX() - i - (x*5));
                     int dy = Math.abs(ship.getY() - j - (y*5));
                     int diff = dx + dy;
                     if(diff < 4 ) {
                         seaLevels[i][j] += 4-diff;
                         hasImpact = true;
                     }

                }
            }
            }
            if(!hasImpact) toDrop.add(key);
        }
        for(int key : toDrop){
            ships.remove(key);
        }
        System.out.println("Sea levels calculated.");
        System.out.println("Sea levels:");
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                System.out.print(seaLevels[i][j]+" ");
            }
            System.out.println();
        }
    }
}
