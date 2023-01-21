package com.anatorini.lab06.Ship;

import com.anatorini.lab06.Util.Pair;
import com.anatorini.lab06.Ship.GUI.ShipFrame;

import java.util.ArrayList;

public class Ship {
    public static String status = "Starting up...";
    public static int x;
    public static int y;
    public static int id;
    public static ArrayList<Pair> scanResults = new ArrayList<>();
    public static ArrayList<String> commandHistory = new ArrayList<>();
    public static void main(String[] args){
        System.out.println("Commencing ship startup sequence...");
        System.out.println("Initializing user interface...");
        ShipFrame shipFrame = new ShipFrame();
        System.out.println("User interface initialized. Awaiting network initialization...");
    }
}
