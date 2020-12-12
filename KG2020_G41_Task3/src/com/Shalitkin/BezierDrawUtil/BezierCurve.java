package com.Shalitkin.BezierDrawUtil;

import com.Shalitkin.PixelDrawers.PixelDrawer;
import com.Shalitkin.ScreenConvertUtil.RealPoint;
import com.Shalitkin.ScreenConvertUtil.ScreenConverter;
import com.Shalitkin.ScreenConvertUtil.ScreenPoint;

import java.awt.*;
import java.util.ArrayList;

public class BezierCurve {
    ArrayList<RealPoint> primaryPoints = new ArrayList<>();
    ArrayList<RealPoint> supportPoints = new ArrayList<>();
    PixelDrawer pd;
    ScreenConverter sc;

    public boolean hasEqualSize(BezierCurve curve){
        return this.supportPoints.size() == curve.supportPoints.size() && this.primaryPoints.size() == curve.primaryPoints.size();
    }

    public BezierCurve() {
    }

    public void addPrimary(RealPoint point) {
        primaryPoints.add(point);
    }

    public void addSupport(RealPoint point) {
        supportPoints.add(point);
    }

    public ArrayList<RealPoint> getPrimaryPoints() {
        return primaryPoints;
    }

    public ArrayList<RealPoint> getSupportPoints() {
        return supportPoints;
    }

    public void drawCurve() {
        if (primaryPoints.size() * 2 - 2 == supportPoints.size() && supportPoints.size() != 0) {
            int j = 0;
            for (int i = 0; i < primaryPoints.size() - 1; i++) {
                drawSegment(primaryPoints.get(i),
                        supportPoints.get(j), supportPoints.get(j + 1), primaryPoints.get(i + 1));
                j+=2;
            }
        } else {
            int j = 0;
            for (int i = 0; i < primaryPoints.size() - 2; i++) {
                drawSegment(primaryPoints.get(i),
                        supportPoints.get(j), supportPoints.get(j + 1), primaryPoints.get(i + 1));
                j+=2;
            }
        }
    }

    public void setPd(PixelDrawer pd) {
        this.pd = pd;
    }

    public void setSc(ScreenConverter sc) {
        this.sc = sc;
    }

    private void drawSegment(RealPoint p0, RealPoint p1, RealPoint p2, RealPoint p3) {
        for (double t = 0; t <= 1; t+= 0.001) {

            double x = Math.pow(1 - t, 3) * p0.getX() + Math.pow(1 - t, 2) * 3 * t * p1.getX() + (1 - t) * 3 * t * t * p2.getX() + t * t * t * p3.getX();
            double y = Math.pow(1 - t, 3) * p0.getY() + Math.pow(1 - t, 2) * 3 * t * p1.getY() + (1 - t) * 3 * t * t * p2.getY() + t * t * t * p3.getY();

            ScreenPoint point = sc.r2s(new RealPoint(x, y));
            pd.setPixel(point.getX(), point.getY(), Color.black);
        }
    }
}
