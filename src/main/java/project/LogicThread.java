package project;

import javax.swing.*;
import java.awt.*;

public class LogicThread extends JFrame implements Runnable {

    private Options options;

    public LogicThread(Options options) throws HeadlessException {
        this.options = options;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < options.getSKIP_FRAMES(); i++) {
                logic();
            }
        }
    }

    private void logic() {
        for (int i = 0; i < options.getFw(); i++) {
            for (int j = 0; j < options.getFh(); j++) {
                Field field = options.getFields()[i][j];
                for (int i1 = 0; i1 < field.particles.size(); i1++) {
                    Particle a = field.particles.get(i1);
                    a.x += a.sx;
                    a.y += a.sy;
                    a.sx *= 0.98f;
                    a.sy *= 0.98f;
                    // velocity normalization
                    // idk if it is still necessary
                    float magnitude = (float) Math.sqrt(a.sx * a.sx + a.sy * a.sy);
                    if (magnitude > 1f) {
                        a.sx /= magnitude;
                        a.sy /= magnitude;
                    }
                    // border repulsion
                    if (a.x < options.getBORDER()) {
                        a.sx += options.getSPEED() * 0.05f;
                        if (a.x < 0) {
                            a.x = -a.x;
                            a.sx *= -0.5f;
                        }
                    } else if (a.x > options.getW() - options.getBORDER()) {
                        a.sx -= options.getSPEED() * 0.05f;
                        if (a.x > options.getW()) {
                            a.x = options.getW() * 2 - a.x;
                            a.sx *= -0.5f;
                        }
                    }
                    if (a.y < options.getBORDER()) {
                        a.sy += options.getSPEED() * 0.05f;
                        if (a.y < 0) {
                            a.y = -a.y;
                            a.sy *= -0.5f;
                        }
                    } else if (a.y > options.getH() - options.getBORDER()) {
                        a.sy -= options.getSPEED() * 0.05f;
                        if (a.y > options.getH()) {
                            a.y = options.getH() * 2 - a.y;
                            a.sy *= -0.5f;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < options.getLinks().size(); i++) {
            Link link = options.getLinks().get(i);
            Particle a = link.a;
            Particle b = link.b;
            float d2 = (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
            if (d2 > options.getMAX_DIST2() / 4) {
                a.links--;
                b.links--;
                a.bonds.remove(b);
                b.bonds.remove(a);
                options.getLinks().remove(link);
                i--;
            } else {
                if (d2 > options.getNODE_RADIUS() * options.getNODE_RADIUS() * 4) {
                    double angle = Math.atan2(a.y - b.y, a.x - b.x);
                    a.sx += (float) Math.cos(angle) * options.getLINK_FORCE() * options.getSPEED();
                    a.sy += (float) Math.sin(angle) * options.getLINK_FORCE() * options.getSPEED();
                    b.sx -= (float) Math.cos(angle) * options.getLINK_FORCE() * options.getSPEED();
                    b.sy -= (float) Math.sin(angle) * options.getLINK_FORCE() * options.getSPEED();
                }
            }
        }
        // moving particle to another field
        for (int i = 0; i < options.getFw(); i++) {
            for (int j = 0; j < options.getFh(); j++) {
                Field field = options.getFields()[i][j];
                for (int i1 = 0; i1 < field.particles.size(); i1++) {
                    Particle a = field.particles.get(i1);
                    if (((int) (a.x / options.getMAX_DIST()) != i) || ((int) (a.y / options.getMAX_DIST()) != j)) {
                        field.particles.remove(a);
                        options.getFields()[(int) (a.x / options.getMAX_DIST())][(int) (a.y / options.getMAX_DIST())].particles.add(a);
                    }
                }
            }
        }
        // dividing scene into parts to reduce complexity
        for (int i = 0; i < options.getFw(); i++) {
            for (int j = 0; j < options.getFh(); j++) {
                Field field = options.getFields()[i][j];
                for (int i1 = 0; i1 < field.particles.size(); i1++) {
                    Particle a = field.particles.get(i1);
                    Particle particleToLink = null;
                    float particleToLinkMinDist2 = (options.getW() + options.getH()) * (options.getW() + options.getH());
                    for (int j1 = i1 + 1; j1 < field.particles.size(); j1++) {
                        Particle b = field.particles.get(j1);
                        float d2 = applyForce(a, b);
                        if (d2 != -1 && d2 < particleToLinkMinDist2) {
                            particleToLinkMinDist2 = d2;
                            particleToLink = b;
                        }
                    }
                    if (i < options.getFw() - 1) {
                        int iNext = i + 1;
                        Field field1 = options.getFields()[iNext][j];
                        for (int j1 = 0; j1 < field1.particles.size(); j1++) {
                            Particle b = field1.particles.get(j1);
                            float d2 = applyForce(a, b);
                            if (d2 != -1 && d2 < particleToLinkMinDist2) {
                                particleToLinkMinDist2 = d2;
                                particleToLink = b;
                            }
                        }
                    }
                    if (j < options.getFh() - 1) {
                        int jNext = j + 1;
                        Field field1 = options.getFields()[i][jNext];
                        for (int j1 = 0; j1 < field1.particles.size(); j1++) {
                            Particle b = field1.particles.get(j1);
                            float d2 = applyForce(a, b);
                            if (d2 != -1 && d2 < particleToLinkMinDist2) {
                                particleToLinkMinDist2 = d2;
                                particleToLink = b;
                            }
                        }
                        if (i < options.getFw() - 1) {
                            int iNext = i + 1;
                            Field field2 = options.getFields()[iNext][jNext];
                            for (int j1 = 0; j1 < field2.particles.size(); j1++) {
                                Particle b = field2.particles.get(j1);
                                float d2 = applyForce(a, b);
                                if (d2 != -1 && d2 < particleToLinkMinDist2) {
                                    particleToLinkMinDist2 = d2;
                                    particleToLink = b;
                                }
                            }
                        }
                    }
                    if (particleToLink != null) {
                        a.bonds.add(particleToLink);
                        particleToLink.bonds.add(a);
                        a.links++;
                        particleToLink.links++;
                        options.getLinks().add(new Link(a, particleToLink));
                    }
                }
            }
        }
    }

    private float applyForce(Particle a, Particle b) {
        float d2 = (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
        boolean canLink = false;
        if(d2 < options.getMAX_DIST2()) {
            float dA = options.getCOUPLING()[a.getType()][b.getType()] / d2;
            float dB = options.getCOUPLING()[b.getType()][a.getType()] / d2;
            if (a.links < options.getLINKS()[a.getType()] && b.links < options.getLINKS()[b.getType()]) {
                if(d2 < options.getMAX_DIST2() / 4) {
                    if (!a.bonds.contains(b) && !b.bonds.contains(a)) {
                        int typeCountA = 0;
                        for (Particle p : a.bonds) {
                            if (p.getType() == b.getType()) typeCountA++;
                        }
                        int typeCountB = 0;
                        for (Particle p : b.bonds) {
                            if (p.getType() == a.getType()) typeCountB++;
                        }
                        if (typeCountA < options.getLINKS_POSSIBLE()[a.getType()][b.getType()] && typeCountB < options.getLINKS_POSSIBLE()[b.getType()][a.getType()]) {
                            canLink = true;
                        }
                    }
                }
            }
            else {
                if (!a.bonds.contains(b) && !b.bonds.contains(a)) {
                    dA = 1 / d2;
                    dB = 1 / d2;
                }
            }
            double angle = Math.atan2(a.y - b.y, a.x - b.x);
            if(d2 < 1) d2 = 1;
            if(d2 < options.getNODE_RADIUS() * options.getNODE_RADIUS() * 4) {
                dA = 1 / d2;
                dB = 1 / d2;
            }
            a.sx += (float)Math.cos(angle) * dA * options.getSPEED();
            a.sy += (float)Math.sin(angle) * dA * options.getSPEED();
            b.sx -= (float)Math.cos(angle) * dB * options.getSPEED();
            b.sy -= (float)Math.sin(angle) * dB * options.getSPEED();
        }
        if(canLink) return d2;
        return -1;
    }
}
