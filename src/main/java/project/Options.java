package project;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Options extends JFrame {
    private final int w = 1600;
    private final int h = 1200;

    private final Color BG = new Color(20, 55, 75, 255);
    private final Color LINK = new Color(255, 230, 0, 100);

    private final int NODE_RADIUS = 10;
    private final int NODE_COUNT = 800;
    private final int MAX_DIST = 100;
    private final int MAX_DIST2 = MAX_DIST * MAX_DIST;
    private final float SPEED = 4f;
    private final int SKIP_FRAMES = 3;
    private final int BORDER = 30;

    private final int fw = w / MAX_DIST + 1;
    private final int fh = h / MAX_DIST + 1;

    volatile public ArrayList<Link> links = new ArrayList<>();
    private final float LINK_FORCE = -0.015f;
    volatile public int frame = 0;

    volatile private BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

    // array for dividing scene into parts to reduce complexity
    private final Field[][] fields = new Field[fw][fh];
    private final float[][] COUPLING = {
            {1, 1, -1, 0},
            {1, 1, 1, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0}
    };

    volatile private int[] LINKS = {
            1,
            3,
            2,
            4
    };

    private final float[][] LINKS_POSSIBLE = {
            {0, 1, 1, 0},
            {1, 2, 1, 0},
            {1, 1, 2, 0},
            {0, 0, 0, 0}
    };

    public Options() {
        for (int i = 0; i < fw; i++) {
            for (int j = 0; j < fh; j++) {
                fields[i][j] = new Field();
            }
        }
        for (int i = 0; i < NODE_COUNT; i++) {
            add((int) (Math.random() * COUPLING.length), (float) (Math.random() * w), (float) (Math.random() * h));
        }
        this.setSize(w + 16, h + 38);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(50, 50);
        this.add(new JLabel(new ImageIcon(img)));
    }

    private Particle add(int type, float x, float y) {
        Particle p = new Particle(ParticleType.values()[type], x, y);
        fields[(int) (p.x / MAX_DIST)][(int) (p.y / MAX_DIST)].particles.add(p);
        return p;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public Color getBG() {
        return BG;
    }

    public Color getLINK() {
        return LINK;
    }

    public int getNODE_RADIUS() {
        return NODE_RADIUS;
    }

    public int getNODE_COUNT() {
        return NODE_COUNT;
    }

    public int getMAX_DIST() {
        return MAX_DIST;
    }

    public int getMAX_DIST2() {
        return MAX_DIST2;
    }

    public float getSPEED() {
        return SPEED;
    }

    public int getSKIP_FRAMES() {
        return SKIP_FRAMES;
    }

    public int getBORDER() {
        return BORDER;
    }

    public int getFw() {
        return fw;
    }

    public int getFh() {
        return fh;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public float getLINK_FORCE() {
        return LINK_FORCE;
    }

    public BufferedImage getImg() {
        return img;
    }

    public Field[][] getFields() {
        return fields;
    }

    public int[] getLINKS() {
        return LINKS;
    }

    public float[][] getCOUPLING() {
        return COUPLING;
    }

    public float[][] getLINKS_POSSIBLE() {
        return LINKS_POSSIBLE;
    }
}
