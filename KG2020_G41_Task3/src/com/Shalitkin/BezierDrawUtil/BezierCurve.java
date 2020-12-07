package com.Shalitkin.BezierDrawUtil;

import com.Shalitkin.PixelDrawers.PixelDrawer;
import com.Shalitkin.ScreenConvertUtil.RealPoint;
import com.Shalitkin.ScreenConvertUtil.ScreenConverter;
import com.Shalitkin.ScreenConvertUtil.ScreenPoint;

import java.awt.*;
import java.util.ArrayList;

public class BezierCurve {
    ArrayList<ScreenPoint> primaryPoints = new ArrayList<>();
    ArrayList<ScreenPoint> supportPoints = new ArrayList<>();
    PixelDrawer pd;
    ScreenConverter sc;

    public boolean hasEqualSize(BezierCurve curve){
        return this.supportPoints.size() == curve.supportPoints.size() && this.primaryPoints.size() == curve.primaryPoints.size();
    }

    public BezierCurve(ScreenConverter sc, PixelDrawer pd) {
        this.sc = sc;
        this.pd = pd;
    }

    public void addPrimary(ScreenPoint point) {
        primaryPoints.add(point);
    }

    public void addSupport(ScreenPoint point) {
        supportPoints.add(point);
    }

    public ArrayList<ScreenPoint> getPrimaryPoints() {
        return primaryPoints;
    }

    public ArrayList<ScreenPoint> getSupportPoints() {
        return supportPoints;
    }

    public void drawCurve() {
        if (primaryPoints.size() * 2 - 2 == supportPoints.size() && supportPoints.size() != 0) {
            int j = 0;
            for (int i = 0; i < primaryPoints.size() - 1; i++) {
                drawSegment(primaryPoints.get(i), primaryPoints.get(i + 1),
                        supportPoints.get(j), supportPoints.get(j + 1));
                j+=2;
            }
        }
    }

    public void setPd(PixelDrawer pd) {
        this.pd = pd;
    }

    private void drawSegment(ScreenPoint p0, ScreenPoint p1, ScreenPoint p2, ScreenPoint p3) {
        for (double t = 0; t <= 1; t+= 0.02) {
            int x = (int) (Math.pow(1 - t, 3) * p0.getX() + Math.pow(1 - t, 2) * 3 * t * p1.getX() + (1 - t) * 3 * t * t * p2.getX() + t * t * t * p3.getX());
            int y = (int) (Math.pow(1 - t, 3) * p0.getY() + Math.pow(1 - t, 2) * 3 * t * p1.getY() + (1 - t) * 3 * t * t * p2.getY() + t * t * t * p3.getY());

            pd.setPixel(x, y, Color.black);
        }
    }
}
