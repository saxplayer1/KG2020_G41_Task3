package com.Shalitkin;



import com.Shalitkin.BezierDrawUtil.BezierCurve;
import com.Shalitkin.PixelDrawers.BufferedImagePixelDrawer;
import com.Shalitkin.PixelDrawers.PixelDrawer;
import com.Shalitkin.ScreenConvertUtil.RealPoint;
import com.Shalitkin.ScreenConvertUtil.ScreenConverter;
import com.Shalitkin.ScreenConvertUtil.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.Shalitkin.Example.makeMiddleCurve;


public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    public DrawPanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }
    private PixelDrawer pd;

    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<Line> crosses = new ArrayList<>();
    private ScreenConverter sc = new ScreenConverter(-2,2,4,4,800,600);

    private BezierCurve curveUp = new BezierCurve();
    private BezierCurve curveDown = new BezierCurve();

    private double dif = 1;

    private Line xAxis = new Line(0,-1,0,1);
    private Line yAxis = new Line(-1,0,1,0);
    private ScreenPoint prevDrag;
    private RealPoint curPoint;
    private Line currentLine = null;

    private int pc1 = 0;
    private int pc2 = 0;

    private RealPoint checkPoints(BezierCurve curve, ScreenPoint point) {
        for(RealPoint p : curve.getPrimaryPoints()) {
            ScreenPoint sp = sc.r2s(p);
            if (Math.pow(sp.getX() - point.getX(), 2) + Math.pow(sp.getY() - point.getY(), 2) < 10) {
                return p;
            }
        }
        for(RealPoint p : curve.getSupportPoints()) {
            ScreenPoint sp = sc.r2s(p);
            if (Math.pow(sp.getX() - point.getX(), 2) + Math.pow(sp.getY() - point.getY(), 2) < 10) {
                return p;
            }
        }
        return null;
    }



    @Override
    public void paint(Graphics g) {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        sc.setScreenW(getWidth());
        sc.setScreenH(getHeight());
        Graphics bi_g = bi.createGraphics();
        bi_g.setColor(Color.white);
        bi_g.fillRect(0,0,getWidth(),getHeight());
        bi_g.dispose();
        pd = new BufferedImagePixelDrawer(bi);
        LineDrawer ld = new BresenhaimLineDrawer(pd);

        for(RealPoint p : curveUp.getPrimaryPoints()) {
            dotToCross(p);
        }
        for(RealPoint p : curveUp.getSupportPoints()) {
            dotToCross(p);
        }

        for(RealPoint p : curveDown.getPrimaryPoints()) {
            dotToCross(p);
        }
        for(RealPoint p : curveDown.getSupportPoints()) {
            dotToCross(p);
        }

        ld.drawLine(new ScreenPoint(0, getHeight() / 3), new ScreenPoint(getWidth(), getHeight() / 3));
        ld.drawLine(new ScreenPoint(0, getHeight() * 2 / 3), new ScreenPoint(getWidth(), getHeight() * 2 / 3));

        for (Line l : lines) {
            drawLine(ld,l);
        }

        for (Line l : crosses) {
            drawLine(ld,l);
        }

        BezierCurve curveMid = makeMiddleCurve(curveUp, curveDown, dif);
        curveUp.setSc(sc);
        curveDown.setSc(sc);
        curveMid.setSc(sc);

        curveUp.setPd(pd);
        curveDown.setPd(pd);
        curveMid.setPd(pd);



        curveUp.drawCurve();
        curveDown.drawCurve();
        curveMid.drawCurve();

        g.drawImage(bi, 0, 0, null);
    }

    private void drawLine (LineDrawer ld, Line l){
        ld.drawLine(sc.r2s(l.getP1()),sc.r2s(l.getP2()));
    }

    private void dotToCross(RealPoint rp) {
        crosses.add(new Line(new RealPoint(rp.getX() - 0.03,rp.getY() - 0.03),
                new RealPoint(rp.getX() + 0.03,rp.getY() + 0.03)));

        crosses.add(new Line(new RealPoint(rp.getX() - 0.03,rp.getY() + 0.03),
                new RealPoint(rp.getX() + 0.03,rp.getY() - 0.03)));
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getY() < getHeight() / 3) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (curveUp.getPrimaryPoints().size() * 2 == curveUp.getSupportPoints().size() || curveUp.getPrimaryPoints().size() == 0) {
                    RealPoint rp;
                    ScreenPoint sp = new ScreenPoint(e.getX(), e.getY());
                    rp = sc.s2r(sp);
                    curveUp.addPrimary(rp);
                }
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (curveUp.getPrimaryPoints().size() * 2 - 2 == curveUp.getSupportPoints().size() || curveUp.getPrimaryPoints().size() * 2 - 1 == curveUp.getSupportPoints().size()) {
                    RealPoint rp;
                    ScreenPoint sp = new ScreenPoint(e.getX(), e.getY());
                    rp = sc.s2r(sp);
                    curveUp.addSupport(rp);
                }
            }
        }

        if (e.getY() > getHeight() * 2 / 3) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (curveDown.getPrimaryPoints().size() * 2 == curveDown.getSupportPoints().size() || curveDown.getPrimaryPoints().size() == 0) {
                    RealPoint rp;
                    ScreenPoint sp = new ScreenPoint(e.getX(), e.getY());
                    rp = sc.s2r(sp);
                    curveDown.addPrimary(rp);
                }
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (curveDown.getPrimaryPoints().size() * 2 - 2 == curveDown.getSupportPoints().size() || curveDown.getPrimaryPoints().size() * 2 - 1 == curveDown.getSupportPoints().size()) {
                    RealPoint rp;
                    ScreenPoint sp = new ScreenPoint(e.getX(), e.getY());
                    rp = sc.s2r(sp);
                    curveDown.addSupport(rp);
                }
            }
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ScreenPoint mousePos = new ScreenPoint(e.getX(), e.getY());
        if (e.getY() < getHeight() / 3) {
            curPoint = checkPoints(curveUp, mousePos);
        }
        if (e.getY() > getHeight() * 2 / 3) {
            curPoint = checkPoints(curveDown, mousePos);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        curPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        ScreenPoint mousePos = new ScreenPoint(e.getX(), e.getY());
        RealPoint p = sc.s2r(mousePos);
        if (curPoint != null) {
            curPoint.setX(p.getX());
            curPoint.setY(p.getY());
            crosses.clear();
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (curveUp.hasEqualSize(curveDown)) {
            int clicks = e.getWheelRotation();
            double c = clicks > 0 ? 0.05 : -0.05;
            for (int i = 0; i < Math.abs(clicks); i++) {
                if (dif < 2 && dif > 0) {
                    dif += c;
                }
            }
            repaint();
        }
    }
}