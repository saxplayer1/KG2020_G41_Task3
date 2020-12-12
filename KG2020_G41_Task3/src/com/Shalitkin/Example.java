package com.Shalitkin;

import com.Shalitkin.BezierDrawUtil.BezierCurve;
import com.Shalitkin.ScreenConvertUtil.RealPoint;

public class Example {
    public static BezierCurve makeMiddleCurve(BezierCurve curveUp, BezierCurve curveDown, double dif){
        BezierCurve curveMid = new BezierCurve();
        if(curveUp.hasEqualSize(curveDown)) {
            double dif1 = 2 - dif;
            for (int i = 0; i < curveUp.getPrimaryPoints().size(); i++) {
                double pX = (curveUp.getPrimaryPoints().get(i).getX() * dif + curveDown.getPrimaryPoints().get(i).getX() * dif1) / 2;
                double pY = (curveUp.getPrimaryPoints().get(i).getY() * dif + curveDown.getPrimaryPoints().get(i).getY() * dif1) / 2;
                curveMid.addPrimary(new RealPoint(pX, pY));
            }

            for (int i = 0; i < curveUp.getSupportPoints().size(); i++) {
                double pX = (curveUp.getSupportPoints().get(i).getX() * dif + curveDown.getSupportPoints().get(i).getX() * dif1) / 2;
                double pY = (curveUp.getSupportPoints().get(i).getY() * dif + curveDown.getSupportPoints().get(i).getY() * dif1) / 2;
                curveMid.addSupport(new RealPoint(pX, pY));
            }
        }
        return curveMid;
    }
}
