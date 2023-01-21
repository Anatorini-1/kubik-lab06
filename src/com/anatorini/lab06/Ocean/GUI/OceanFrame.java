package com.anatorini.lab06.Ocean.GUI;

import javax.swing.*;

public class OceanFrame extends JFrame {
    private OceanPanel oceanPanel;

    public OceanFrame(){
        super();
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oceanPanel = new OceanPanel();
        add(oceanPanel);
        setVisible(true);
    }

}
