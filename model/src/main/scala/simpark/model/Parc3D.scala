package simpark.model

import viabilitree.model.Dynamic

case class Parc3D(
  integrationStep: Double = 0.01,
  timeStep: Double = 0.05,
  alpha: Double= 0.1,
  ip: Double = 0.01,
  it: Double = 0.01,
  l: Double = 0.00052,
  g: Double = 0.2,
  M: Double = 36036.0,
  p: Double = 0.0004,
  a: Double = 10000.0,
  e: Double = 100.0,
  eta: Double = 0.0008,
  phi: Double = 1833.0,
  phi2: Double = 10000.0,
  del: Double = 0.01,
  mp: Double = 14.0,
  mt: Double = 25.0) {

  def dynamic(state: Vector[Double], control: Vector[Double]) = {
    // A: state(0), T: state(1), E: state(2)
    def CDot(state: Vector[Double], t: Double) =  -del * state(0) + p * state(1) * mp * ip +  state(2) * mt * it
    def ADot(state: Vector[Double], t: Double) =  state(1) * g * (1 - state(1) / (1 + M / (1 + eta * state(2) / (control(1) + 1)))) - control(0) * l * state(1) * state(2) - p * state(1)
    def TDot(state: Vector[Double], t: Double) =  state(2) * (- alpha * state(2) + (a * control(0) * state(1))/(state(1) + phi) + e*state(0) /(state(0)+ phi2*state(2) + phi2))

    val dynamic = Dynamic(ADot, TDot, CDot)
    dynamic.integrate(state.toArray, integrationStep, timeStep)
  }



}
