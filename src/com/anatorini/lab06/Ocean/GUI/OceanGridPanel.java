package com.anatorini.lab06.Ocean.GUI;

import com.anatorini.lab06.Ocean.Core.ShipModel;
import com.anatorini.lab06.Ocean.Ocean;

import javax.swing.*;
import java.awt.*;

public class OceanGridPanel extends JPanel {
    public OceanGridPanel(){
        super();
        new Painter(this).start();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(Ocean.oceanWidth <= 0) return;
        float dx = (float) getWidth() / Ocean.oceanWidth;
        float dy = (float) getHeight() / Ocean.oceanHeight;
        float dxBy5 = dx / 5;
        float dyBy5 = dy / 5;
        drawBuoy(g2d, dx, dy);
        drawGrid(g2d, dx, dy, dxBy5, dyBy5);
        drawShips(g2d, dx, dy, dxBy5, dyBy5);
    }
    private void drawGrid(Graphics2D g2d,float dx,float dy,float dxBy5, float dyBy5){
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1));
        for(int i = 0; i < Ocean.oceanWidth*5; i++)
            g2d.drawLine((int) (i * dxBy5), 0, (int) (i * dxBy5), getHeight());
        for(int i = 0; i < Ocean.oceanHeight*5; i++)
            g2d.drawLine(0, (int) (i * dyBy5), getWidth(), (int) (i * dyBy5));
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        for(int i = 0; i < Ocean.oceanWidth; i++)
            g2d.drawLine((int) (i * dx), 0, (int) (i * dx), getHeight());
        for(int i = 0; i < Ocean.oceanHeight; i++)
            g2d.drawLine(0, (int) (i * dy), getWidth(), (int) (i * dy));


    }
    private void drawShips(Graphics2D g2d,float dx,float dy,float dxBy5, float dyBy5){
        g2d.setColor(Color.GREEN);
        for(Integer i : Ocean.ships.keySet()){
            ShipModel sm = Ocean.ships.get(i);
            if(sm==null || sm.getState().equals("SUNKEN")) continue;
            g2d.fillRect((int) (sm.getX() * dxBy5), (int) (sm.getY() * dyBy5), (int)dxBy5, (int)dyBy5);
        }
    }
    private void drawBuoy(Graphics2D g2d,float dx,float dy){
        for(int i=0;i<Ocean.oceanWidth;i++){
            for(int j=0;j<Ocean.oceanHeight;j++){
                if(Ocean.buoys[i][j] != null)
                    g2d.setColor(new Color(56, 56, 255, 77));
                else
                    g2d.setColor(new Color(255, 46, 46, 110));
                g2d.fillRect((int) (i * dx), (int) (j * dy), (int) dx, (int) dy);
            }
        }
    }

    private static class Painter extends Thread{
        private OceanGridPanel panel;
        public Painter(OceanGridPanel panel){
            this.panel = panel;
        }
        @Override
        public void run(){
            try{
                while(true){
                    panel.repaint();
                    Thread.sleep(100);
                }
            }catch (InterruptedException ignored){
            }
        }
    }
}
