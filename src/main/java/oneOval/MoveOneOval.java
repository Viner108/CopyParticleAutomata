package oneOval;

import project.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MoveOneOval extends JFrame implements Runnable {
    private Options options = new Options();
    private final Field[][] fields = new Field[options.getFw()][options.getFh()];

    public MoveOneOval() {
        for (int i = 0; i < options.getFw(); i++) {
            for (int j = 0; j < options.getFh(); j++) {
                fields[i][j] = new Field();
            }
        }
        add( (float) (Math.random() * options.getW()), (float) (Math.random() * options.getH()));
        this.setSize(options.getW() + 16, options.getH() + 38);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(50, 50);
        this.add(new JLabel(new ImageIcon(options.getImg())));
    }

    private Particle add( float x, float y) {
        Particle p = new Particle(ParticleType.RED, x, y);
        fields[(int) (p.x / options.getMAX_DIST())][(int) (p.y / options.getMAX_DIST())].particles.add(p);
        return p;
    }


    @Override
    public void run() {
        Thread thread = new Thread(new LogicThread(options));
        thread.start();
        while (true) {
            this.repaint();
        }
    }

    public void paint(Graphics g) {
        drawOneParticle();

        ((Graphics2D) g).drawImage(options.getImg(), null, 8, 30);
        options.frame++;
    }

    private void drawOneParticle() {
        Graphics2D g2 = options.getImg().createGraphics();
        g2.setColor(options.getBG());
        g2.fillRect(0, 0, options.getW(), options.getH());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Field field = options.getFields()[2][2];
        Particle a = field.particles.get(1);
        g2.setColor(a.getColor());
        g2.fillOval((int) a.x - options.getNODE_RADIUS(), (int) a.y - options.getNODE_RADIUS(), options.getNODE_RADIUS() * 2, options.getNODE_RADIUS() * 2);

    }
}
