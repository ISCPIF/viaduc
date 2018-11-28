package fr.iscpif.client

object CAT_RK4 {


  def compute_CAT_RK4(zeta: Double, l: Double, g: Double, M: Double, h_C: Double,
                      c: Double, p: Double, a: Double, e: Double, eta: Double,
                      eps: Double, phi: Double, d: Double, delta: Double,
                      mp: Double, mt: Double, Cprev : Double, Aprev: Double, Tprev: Double): Array[Double] = {

    def C(zeta: Double, l: Double, g: Double, M: Double, h: Double,
          c: Double, p: Double, a: Double, e: Double, eta: Double,
          eps: Double, phi: Double, d: Double, delta: Double,
          mp: Double, mt: Double, Cprev : Double, Aprev: Double, Tprev: Double): Double = {

      -delta * Cprev + p* Aprev * mp + Tprev * mt
    }

    def A(zeta: Double, l: Double, g: Double, M: Double, h: Double,
          c: Double, p: Double, a: Double, e: Double, eta: Double,
          eps: Double, phi: Double, d: Double, delta: Double,
          mp: Double, mt: Double, Cprev : Double, Aprev: Double, Tprev: Double): Double = {

      Aprev * g * ( 1 - Aprev/ (1+ 1/(M/(1+eta*Tprev/(eps+1))))) - zeta*l*Aprev*Tprev - p*Aprev
    }

    def T(zeta: Double, l: Double, g: Double, M: Double, h: Double,
          c: Double, p: Double, a: Double, e: Double, eta: Double,
          eps: Double, phi: Double, d: Double, delta: Double,
          mp: Double, mt: Double, Cprev : Double, Aprev: Double, Tprev: Double): Double = {

      Tprev *( - c*Tprev/(Tprev+ phi) - d) + zeta*a*Aprev
    }

    val h= 0.0000001

    val k1_C = C(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev  , Aprev , Tprev )

    val k1_A = A(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev  , Aprev , Tprev )

    val k1_T = T(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev  , Aprev , Tprev )



    val k2_C = C(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev + (h/2)*k1_C  , Aprev + (h/2)*k1_A , Tprev + (h/2)*k1_T )

    val k2_A = A(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev + (h/2)*k1_C  , Aprev + (h/2)*k1_A , Tprev + (h/2)*k1_T )

    val k2_T = T(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev + (h/2)*k1_C  , Aprev + (h/2)*k1_A , Tprev + (h/2)*k1_T )



    val k3_C = C(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev + (h/2)*k2_C  , Aprev + (h/2)*k2_A , Tprev + (h/2)*k2_T )

    val k3_A = A(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev + (h/2)*k2_C  , Aprev + (h/2)*k2_A , Tprev + (h/2)*k2_T )

    val k3_T = T(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev + (h/2)*k2_C  , Aprev + (h/2)*k2_A , Tprev + (h/2)*k2_T )



    val k4_C = C(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev + h*k3_C  , Aprev + h*k3_A , Tprev + h*k3_T )

    val k4_A = A(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev + h*k3_C  , Aprev + h*k3_A , Tprev + h*k3_T )

    val k4_T = T(zeta, l, g , M , h_C , c , p , a , e , eta , eps
      , phi , d , delta , mp , mt , Cprev + h*k3_C  , Aprev + h*k3_A , Tprev + h*k3_T )


    val Cnew = Cprev + h/6 *(k1_C + 2*k2_C + 2*k3_C + k4_C)
    val Anew = Aprev + h/6 *(k1_A + 2*k2_A + 2*k3_A + k4_A)
    val Tnew = Tprev + h/6 *(k1_T + 2*k2_T + 2*k3_T + k4_T)

    Array(Cnew, Anew, Tnew)

  }

}
