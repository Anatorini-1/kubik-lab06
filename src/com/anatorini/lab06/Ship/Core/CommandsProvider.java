package com.anatorini.lab06.Ship.Core;

import com.anatorini.lab06.Ship.Ship;
import com.anatorini.lab06.Util.Pair;

import java.util.ArrayList;

public class CommandsProvider {
    public static boolean move(){
        String direction = "";
        int randDir = (int)(Math.random() * 8);
        switch (randDir){
            case 0:
                direction = "N";
                break;
            case 1:
                direction = "NE";
                break;
            case 2:
                direction = "E";
                break;
            case 3:
                direction = "SE";
                break;
            case 4:
                direction = "S";
                break;
            case 5:
                direction = "SW";
                break;
            case 6:
                direction = "W";
                break;
            case 7:
                direction = "NW";
                break;
        }
        Ship.commandHistory.add("MOVE " + direction);
        System.out.println("Sent command: MOVE;"  + direction + "\n");
        System.out.println("Awaiting response...");
        String result = NetworkManager.sendCommand("MOVE;SHIP;" + NetworkManager.shipId+";" + direction + "\n");
        String[] args = result.split(";");
        System.out.println("Received: " + result);
        if(args[0].equals("OK")){
            Ship.x = Integer.parseInt(args[1]);
            Ship.y = Integer.parseInt(args[2]);
            return true;
        }
        else{
            System.out.println("Error: " + args[1]);
            return true;
        }
    }
    public static boolean scan(){
        Ship.commandHistory.add("SCAN");
        System.out.println("Sent command: SCAN;");
        System.out.println("Awaiting response...");
        String result = NetworkManager.sendCommand("SCAN;SHIP;" + NetworkManager.shipId + "\n");
        String[] args = result.split(";");
        System.out.println("Updating scan results...");
        Ship.scanResults = new ArrayList<>();
        if(args.length > 1){
            //SCAN_RESULT;1,2:2,3:4,5;
            String[] coords = args[1].split(":");
            Ship.scanResults = new ArrayList<>();
            for(String coord : coords){
                String[] xy = coord.split(",");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                Ship.scanResults.add(new Pair(x,y));
            }
        }
        System.out.println("Scan results updated. Found " + Ship.scanResults.size() + " ships.");
        return true;
    }
}
