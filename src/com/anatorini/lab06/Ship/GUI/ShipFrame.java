package com.anatorini.lab06.Ship.GUI;

import javax.swing.*;

public class ShipFrame extends JFrame {
    private ShipPanel shipPanel;
    public ShipFrame(){
        super("Ship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        shipPanel = new ShipPanel();
        add(shipPanel);
        setResizable(false);
        setVisible(true);
    }
}
