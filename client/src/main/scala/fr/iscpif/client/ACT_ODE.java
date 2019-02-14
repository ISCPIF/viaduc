//package fr.iscpif.client;
//import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
//
//public  class ACT_ODE implements FirstOrderDifferentialEquations {
//
//    private double[] c;
//    private double omega;
//
//    public void CircleODE(double[] c, double omega) {
//        this.c     = c;
//        this.omega = omega;
//    }
//
//    public int getDimension() {
//        return 2;
//    }
//
//    public void computeDerivatives(double t, double[] y, double[] yDot) {
//        yDot[0] = omega * (c[1] - y[1]);
//        yDot[1] = omega * (y[0] - c[0]);
//    }
//
//}
