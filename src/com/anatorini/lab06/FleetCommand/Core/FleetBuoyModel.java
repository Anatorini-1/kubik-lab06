package com.anatorini.lab06.FleetCommand.Core;

import lombok.Data;

import java.net.Socket;

@Data
public class FleetBuoyModel {
    private int x;
    private int y;
    private Socket socket;
    private int[][] data;
    public FleetBuoyModel(int x, int y, Socket socket){
        this.x = x;
        this.y = y;
        this.socket = socket;
        data = new int[5][5];
        for(int i=0;i<5;i++)
            for (int j=0;j<5;j++)
                data[i][j] = 0;
    }


}
