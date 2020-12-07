package com.Shalitkin;

import com.Shalitkin.PixelDrawers.PixelDrawer;
import com.Shalitkin.ScreenConvertUtil.ScreenPoint;

import java.awt.*;

public class BresenhaimLineDrawer implements LineDrawer {
    private PixelDrawer pd;

    public BresenhaimLineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawLine(ScreenPoint p1, ScreenPoint p2) {
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;
        int xstart = p1.getX();
        int xend = p2.getX();
        int ystart = p1.getY();
        int yend = p2.getY();
        dx = xend - xstart;
        dy = yend - ystart;

        incx = sign(dx);
        incy = sign(dy);

        dx = Math.abs(dx);
        dy = Math.abs(dy);

        if (dx > dy) {
            pdx = incx; pdy = 0;
            es = dy; el = dx;
        } else {
            pdx = 0; pdy = incy;
            es = dx; el = dy;
        }

        x = xstart;
        y = ystart;
        err = el / 2;
        pd.setPixel(x, y,Color.black);

        for (int t = 0; t < el; t++) {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;
                y += incy;
            }
            else {
                x += pdx;
                y += pdy;
            }

            pd.setPixel(x, y,Color.black);
        }


    }
    private int sign(int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
    }
}
