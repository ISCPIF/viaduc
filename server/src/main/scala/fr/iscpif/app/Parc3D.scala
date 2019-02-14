package fr.iscpif.app
import viabilitree.model._

case class Parc3D() {
  var integrationStep = 0.01
  var timeStep = 0.05
  var alpha = 0.1
  var ip = 0.01
  var it = 0.01
  var l = 0.00052
  var g = 0.2
  var M = 36036.0
  var p = 0.0004
  var a = 10000.0
  var e = 100.0
  var eta = 0.0008
  var phi = 1833.0
  var phi2 = 10000.0
  var del = 0.01
  var mp = 14.0
  var mt = 25.0

    def dynamic(state: Vector[Double], control: Vector[Double]) = {
      // A: state(0), T: state(1), E: state(2)
      def CDot(state: Vector[Double], t: Double) =  -del * state(0) + p * state(1) * mp * ip +  state(2) * mt * it

      def ADot(state: Vector[Double], t: Double) =  state(1) * g * (1 - state(1) / (1 + M / (1 + eta * state(2) / (control(1) + 1)))) - control(0) * l * state(1) * state(2) - p * state(1)

      def TDot(state: Vector[Double], t: Double) =  state(2) * (- alpha * state(2) + (a * control(0) * state(1))/(state(1) + phi) + e*state(0) /(state(0)+ phi2*state(2) + phi2))


      val dynamic = Dynamic(ADot, TDot, CDot)
      dynamic.integrate(state.toArray, integrationStep, timeStep)
    }



}
