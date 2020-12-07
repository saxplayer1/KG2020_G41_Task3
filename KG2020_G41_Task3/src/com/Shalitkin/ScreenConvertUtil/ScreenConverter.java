package com.Shalitkin.ScreenConvertUtil;

public class ScreenConverter {
    private double x, y, w, h;
    private int screenW, screenH;

    public ScreenConverter(double x, double y, double w, double h, int screenW, int screenH) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.screenW = screenW;
        this.screenH = screenH;
    }

    public ScreenPoint r2s(RealPoint p){
        int px = (int)((p.getX() - x) * screenW/w);
        int py = (int)((y - p.getY()) * screenH/h);
        return new ScreenPoint(px,py);
    }

    public RealPoint s2r (ScreenPoint p) {
        double px = p.getX() * w / screenW + x;
        double py = y - p.getY() * h / screenH;
        return new RealPoint(px, py);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public int getScreenW() {
        return screenW;
    }

    public void setScreenW(int screenW) {
        this.screenW = screenW;
    }

    public int getScreenH() {
        return screenH;
    }

    public void setScreenH(int screenH) {
        this.screenH = screenH;
    }
}
