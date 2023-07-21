package oneOval;

import project.*;

import javax.swing.*;
import java.awt.*;

public class MoveOneOval extends JFrame implements Runnable {
    private Options options = new Options();

    @Override
    public void run() {
        while (true) {
            Graphics2D g2 = options.getImg().createGraphics();
            g2.setColor(options.getBG());
            g2.fillRect(0, 0, options.getW(), options.getH());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Field field = options.getFields()[2][2];
            Particle a = field.particles.get(2);
            g2.setColor(a.getColor());
            g2.fillOval((int) a.x - options.getNODE_RADIUS(), (int) a.y - options.getNODE_RADIUS(), options.getNODE_RADIUS() * 2, options.getNODE_RADIUS() * 2);
//            new Thread(new LogicThread(options)).start();
        }
    }
}
