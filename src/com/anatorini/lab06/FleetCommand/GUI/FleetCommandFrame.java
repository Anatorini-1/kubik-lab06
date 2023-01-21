package com.anatorini.lab06.FleetCommand.GUI;

import javax.swing.*;

public class FleetCommandFrame extends JFrame {
    public FleetCommandFrame(){
        super();
        setSize(725,600);
        FleetCommandPanel fcp = new FleetCommandPanel(this.getWidth(),this.getHeight());
        add(fcp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }
}
