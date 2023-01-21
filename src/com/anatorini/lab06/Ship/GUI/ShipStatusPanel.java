package com.anatorini.lab06.Ship.GUI;

import com.anatorini.lab06.Ship.Ship;

import javax.swing.*;
import java.awt.*;

public class ShipStatusPanel extends JPanel {
    private JLabel shipStatusLabel;
    private JLabel knownShipsLabel;
    private JLabel recentCommandsLabel;
    private JLabel shipStatus;
    private JLabel knownShips;
    private JLabel recentCommands;

    public ShipStatusPanel(){
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        shipStatusLabel = new JLabel("Ship Status:");
        add(shipStatusLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        shipStatus = new JLabel(Ship.status);
        add(shipStatus, c);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.weighty = 0.5;
        knownShipsLabel = new JLabel("Known Ships:");
        add(knownShipsLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        c.weighty = 0.5;
        knownShips = new JLabel(Ship.scanResults.toString());
        add(knownShips, c);
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
        recentCommandsLabel = new JLabel("Recent Commands:");
        add(recentCommandsLabel, c);
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
        recentCommands = new JLabel(Ship.commandHistory.toString());
        add(recentCommands, c);
        new Updater().start();
    }
    private class Updater extends Thread{
        public void run(){
            while(true){
                try{
                    Thread.sleep(100);
                    shipStatus.setText(Ship.status);
                    knownShips.setText("<html>"+Ship.scanResults.toString()+"</html>");
                    String s = "<html>";
                    int j=0;
                    for(int i=Ship.commandHistory.size(); i > 0 && j < 5;i--){
                        s = s.concat(Ship.commandHistory.get(i-1) + "<br>");
                        j++;
                    }
                    s = s.concat("</html>");
                    recentCommands.setText(s);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
