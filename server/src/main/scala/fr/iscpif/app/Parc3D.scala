package fr.iscpif.app



import viabilitree.model._
class Parc3D(
                     integrationStep: Double = 0.01,
                     timeStep: Double = 0.05,
                     //zeta: Double = 0.01,
                     l: Double = 0.01,
                     g: Double = 1.0,
                     M: Double = 5000.0,
                     c: Double = 0.01,
                     p: Double = 0.3,
                     a: Double = 100.0,
                     e: Double = 0.001,
                     eta: Double = 0.0005,
                     //eps: Double = 10.0,
                     phi: Double = 1.0,
                     phi2: Double = 1.0,
                     d: Double = 1.0,
                     del: Double = 0.1,
                     h: Double = 12000.0,
                     mp: Double = 20.0,
                     mt: Double = 0.5

                     // valeurs de controle: eps control(1) et zeta control(0)
                   ) {

    def dynamic(state: Vector[Double], control: Vector[Double]) = {
      // A: state(0), T: state(1), E: state(2)
      def CDot(state: Vector[Double], t: Double)= -del * state(0) + p*state(1)*mp + mt*state(2)-h

      def ADot(state: Vector[Double], t: Double) = state(1) * g * (1 - state(1)/(1+ M /(1+eta*state(2)/(control(1)+1)))) -  control(0) * l * state(1) * state(2) - p*state(1)

      def TDot(state: Vector[Double], t: Double) = state(2) * ( - c * state(2)/(state(2)+phi)+e * state(0)/(state(0)+phi2)-d) + a * control(0)* state(1)


      val dynamic = Dynamic(ADot, TDot, CDot)
      dynamic.integrate(state.toArray, integrationStep, timeStep)
    }

  }


}
