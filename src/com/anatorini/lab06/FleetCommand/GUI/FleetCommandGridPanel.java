package com.anatorini.lab06.FleetCommand.GUI;

import com.anatorini.lab06.FleetCommand.FleetCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class FleetCommandGridPanel extends JPanel {

    public FleetCommandGridPanel() {
        super();
        setBackground(Color.WHITE);
        new Painter(this).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        if (FleetCommand.oceanHeight == 0 || FleetCommand.oceanWidth == 0) return;
        float dx = (float) getWidth() / FleetCommand.oceanWidth;
        float dy = (float) getHeight() / FleetCommand.oceanHeight;
        float dxBy5 = dx / 5;
        float dyBy5 = dy / 5;
        drawBuoyData(g2d,dx,dy);
        drawGrid(g2d,dx,dy,dxBy5,dyBy5);
        g2d.setColor(Color.BLACK);


    }

    private void drawGrid(Graphics2D g2d,float dx,float dy,float dxBy5,float dyBy5){
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1));
        for(int i = 0; i < FleetCommand.oceanWidth*5; i++)
            g2d.drawLine((int) (i * dxBy5), 0, (int) (i * dxBy5), getHeight());
        for(int i = 0; i < FleetCommand.oceanHeight*5; i++)
            g2d.drawLine(0, (int) (i * dyBy5), getWidth(), (int) (i * dyBy5));
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        for(int i = 0; i < FleetCommand.oceanWidth; i++)
            g2d.drawLine((int) (i * dx), 0, (int) (i * dx), getHeight());
        for(int i = 0; i < FleetCommand.oceanHeight; i++)
            g2d.drawLine(0, (int) (i * dy), getWidth(), (int) (i * dy));


    }

    private void drawBuoyData(Graphics2D g2d,float dx,float dy) {
        float dx2 = dx / 5;
        float dy2 = dy / 5;
        AffineTransform old = g2d.getTransform();
        AffineTransform at = new AffineTransform();
        g2d.setTransform(at);
        for (int i = 0; i < FleetCommand.oceanWidth; i++) {
            for (int j = 0; j < FleetCommand.oceanHeight; j++) {
                int tmp;
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        tmp = FleetCommand.fleetBuoyModels[i][j] != null ? FleetCommand.fleetBuoyModels[i][j].getData()[k][l] : 10;
                        Color c = switch (tmp) {
                            case 0 -> Color.WHITE;
                            case 1 -> Color.BLUE;
                            case 2 -> Color.GREEN;
                            case 3 -> Color.YELLOW;
                            case 4 -> Color.RED;
                            case 5 -> new Color(141, 0, 0);
                            case 6 -> Color.MAGENTA;
                            case 10 -> new Color(89, 188, 255);
                            default -> Color.BLACK;
                        };
                        g2d.setColor(c);
                        g2d.fillRect((int)(k * dx2), (int)(l * dy2), (int)dx2, (int)dy2);
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(String.valueOf(tmp), (int)(k * dx2)+dx/2, (int)(l * dy2)+dy/2);
                    }
                }
                g2d.translate(0,dy);
            }
            g2d.translate(dx,-dy * FleetCommand.oceanHeight);
        }
        g2d.setTransform(old);

    }

    private static class Painter extends Thread {
        private FleetCommandGridPanel panel;

        public Painter(FleetCommandGridPanel panel) {
            super();
            this.panel = panel;
        }

        @Override
        public void run() {
            while (true) {
                panel.repaint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
