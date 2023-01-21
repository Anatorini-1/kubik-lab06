package com.anatorini.lab06.FleetCommand;

import com.anatorini.lab06.FleetCommand.Core.FleetBuoyModel;
import com.anatorini.lab06.FleetCommand.Core.NetworkManager;
import com.anatorini.lab06.FleetCommand.GUI.FleetCommandFrame;

import java.net.Socket;

public  class FleetCommand {
    public static int oceanWidth;
    public static int oceanHeight;
    public static NetworkManager networkManager;
    public static FleetBuoyModel[][] fleetBuoyModels;

    public static void main(String[] args){
        networkManager = new NetworkManager();
        FleetCommandFrame fcf = new FleetCommandFrame();

    }
}
