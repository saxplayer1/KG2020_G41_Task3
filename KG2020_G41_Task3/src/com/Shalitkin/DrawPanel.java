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


public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    public DrawPanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }
    private PixelDrawer pd;

    private ArrayList<Line> lines = new ArrayList<>();
    private ScreenConverter sc = new ScreenConverter(-2,2,4,4,800,600);

    private BezierCurve curveUp = new BezierCurve(sc, pd);
    private BezierCurve curveMid = new BezierCurve(sc, pd);
    private BezierCurve curveDown = new BezierCurve(sc, pd);

    private double dif = 1;

    private Line xAxis = new Line(0,-1,0,1);
    private Line yAxis = new Line(-1,0,1,0);
    private ScreenPoint prevDrag;
    private Line currentLine = null;

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

        for(ScreenPoint p : curveUp.getPrimaryPoints()) {
            dotToCross(p);
        }
        for(ScreenPoint p : curveUp.getSupportPoints()) {
            dotToCross(p);
        }

        for(ScreenPoint p : curveDown.getPrimaryPoints()) {
            dotToCross(p);
        }
        for(ScreenPoint p : curveDown.getSupportPoints()) {
            dotToCross(p);
        }

        ld.drawLine(new ScreenPoint(0, getHeight() / 3), new ScreenPoint(getWidth(), getHeight() / 3));
        ld.drawLine(new ScreenPoint(0, getHeight() * 2 / 3), new ScreenPoint(getWidth(), getHeight() * 2 / 3));

        for (Line l : lines) {
            drawLine(ld,l);
        }

        curveUp.setPd(pd);
        curveDown.setPd(pd);
        curveMid.setPd(pd);
        g.drawImage(bi, 0, 0, null);

        curveUp.drawCurve();
        curveDown.drawCurve();
        if(curveUp.hasEqualSize(curveDown)) {
            double dif1 = 2 - dif;
            curveMid.getPrimaryPoints().clear();
            curveMid.getSupportPoints().clear();
            for (int i = 0; i < curveUp.getPrimaryPoints().size(); i++) {
                int pX = (int) ((curveUp.getPrimaryPoints().get(i).getX() * dif + curveDown.getPrimaryPoints().get(i).getX() * dif1) / 2);
                int pY = (int) ((curveUp.getPrimaryPoints().get(i).getY() * dif + curveDown.getPrimaryPoints().get(i).getY() * dif1) / 2);
                curveMid.addPrimary(new ScreenPoint(pX, pY));
            }

            for (int i = 0; i < curveUp.getSupportPoints().size(); i++) {
                int pX = (int) ((curveUp.getSupportPoints().get(i).getX() * dif + curveDown.getSupportPoints().get(i).getX() * dif1) / 2);
                int pY = (int) ((curveUp.getSupportPoints().get(i).getY() * dif + curveDown.getSupportPoints().get(i).getY() * dif1) / 2);
                curveMid.addSupport(new ScreenPoint(pX, pY));
            }
        }
        curveMid.drawCurve();
    }

    private void drawLine (LineDrawer ld, Line l){
        ld.drawLine(sc.r2s(l.getP1()),sc.r2s(l.getP2()));
    }

    private void dotToCross(ScreenPoint sp) {
        RealPoint point = sc.s2r(sp);
        lines.add(new Line(new RealPoint(point.getX() - 0.03,point.getY() - 0.03),
                new RealPoint(point.getX() + 0.03,point.getY() + 0.03)));

        lines.add(new Line(new RealPoint(point.getX() - 0.03,point.getY() + 0.03),
                new RealPoint(point.getX() + 0.03,point.getY() - 0.03)));
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getY() < getHeight() / 3) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (curveUp.getPrimaryPoints().size() * 2  == curveUp.getSupportPoints().size() || curveUp.getPrimaryPoints().size() == 0) {
                    curveUp.addPrimary(new ScreenPoint(e.getX(), e.getY()));
                    pd.setPixel(e.getX(), e.getY(), Color.black);
                }
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (curveUp.getPrimaryPoints().size() * 2 - 2 == curveUp.getSupportPoints().size() || curveUp.getPrimaryPoints().size() * 2 - 1 == curveUp.getSupportPoints().size()) {
                    curveUp.addSupport(new ScreenPoint(e.getX(), e.getY()));
                    pd.setPixel(e.getX(), e.getY(), Color.red);
                }
            }
        }

        if (e.getY() > getHeight() * 2 / 3) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (curveDown.getPrimaryPoints().size() * 2  == curveDown.getSupportPoints().size() || curveDown.getPrimaryPoints().size() == 0) {
                    curveDown.addPrimary(new ScreenPoint(e.getX(), e.getY()));
                    pd.setPixel(e.getX(), e.getY(), Color.black);
                }
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (curveDown.getPrimaryPoints().size() * 2 - 2 == curveDown.getSupportPoints().size() || curveDown.getPrimaryPoints().size() * 2 - 1 == curveDown.getSupportPoints().size()) {
                    curveDown.addSupport(new ScreenPoint(e.getX(), e.getY()));
                    pd.setPixel(e.getX(), e.getY(), Color.red);
                }
            }
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (curveUp.hasEqualSize(curveDown)) {
            int clicks = e.getWheelRotation();
            dif = 1;
            double c = clicks > 0 ? 0.1 : -0.1;
            for (int i = 0; i < Math.abs(clicks); i++) {
                if (dif <= 2 && dif >= 0) {
                    dif += c;
                }
            }
            repaint();
        }
    }
}
