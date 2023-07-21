package project;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Form extends JFrame implements Runnable {
    Options options=new Options();


    public Form() {
//        for (int i = 0; i < options.getFw(); i++) {
//            for (int j = 0; j < options.getFh(); j++) {
//                options.getFields()[i][j] = new Field();
//            }
//        }
//        // put particles randomly
//        for (int i = 0; i < options.getNODE_COUNT(); i++) {
//            add((int)(Math.random() * options.getCOUPLING().length), (float)(Math.random() * options.getW()), (float)(Math.random() * options.getH()));
//        }
//
//        this.setSize(options.getW() + 16, options.getH() + 38);
//        this.setVisible(true);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLocation(50, 50);
//        this.add(new JLabel(new ImageIcon(options.getImg())));
    }

//    private Particle add(int type, float x, float y) {
//        Particle p = new Particle(ParticleType.values()[type], x, y);
//        options.getFields()[(int) (p.x / options.getMAX_DIST())][(int) (p.y / options.getMAX_DIST())].particles.add(p);
//        return p;
//    }

    @Override
    public void run() {
        Thread thread=new Thread(new LogicThread(options));
        thread.start();
        while(true) {
            this.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        drawScene(options.getImg());
        ((Graphics2D)g).drawImage(options.getImg(), null, 8, 30);
        options.frame++;
    }

    private void drawScene(BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        g2.setColor(options.getBG());
        g2.fillRect(0, 0, options.getW(), options.getH());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < options.getFw(); i++) {
            for (int j = 0; j < options.getFh(); j++) {
                Field field = options.getFields()[i][j];
                for (int i1 = 0; i1 < field.particles.size(); i1++) {
                    Particle a = field.particles.get(i1);
                    g2.setColor(a.getColor());
                    g2.fillOval((int) a.x - options.getNODE_RADIUS(), (int) a.y - options.getNODE_RADIUS(), options.getNODE_RADIUS() * 2, options.getNODE_RADIUS() * 2);
                }
            }
        }
        g2.setColor(options.getLINK());
        for (Link link: options.getLinks()) {
            g2.drawLine((int) link.a.x, (int) link.a.y, (int) link.b.x, (int) link.b.y);
        }
    }
}
