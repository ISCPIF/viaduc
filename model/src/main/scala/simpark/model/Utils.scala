package simpark.model

object Utils {

  def compute_CAT_RK4(zeta: Double, l: Double, g: Double, M: Double,
                      alpha: Double, p: Double, a: Double, e: Double, eta: Double,
                      eps: Double, phi: Double, phi2: Double, delta: Double,
                      mp: Double, mt: Double,ip: Double,it: Double, Cprev : Double, Aprev: Double, Tprev: Double): Array[Double] = {

    def C(zeta: Double, l: Double, g: Double, M: Double,
          alpha: Double, p: Double, a: Double, e: Double, eta: Double,
          eps: Double, phi: Double,phi2: Double, delta: Double,
          mp: Double, mt: Double,ip: Double,it: Double, Cprev : Double, Aprev: Double, Tprev: Double): Double = {

      /* (-del)*y(1) + p*y(2)*(mp)*ip+mt*y(3)*it  // Capital
     f1(2) = y(2)*g*(1-(y(2)/(1+M/(1+eta*y(3)/(1+eps)))))-zeta*l*y(3)*y(2)-p*y(2) //animaux A
     f1(3)= y(3)*(-alpha*y(3) + a*zeta*y(2)/(y(2)+phi) + e*y(1)/(y(1)+phi2*y(3)+phi2));
       }*/

      -delta * Cprev + p* Aprev * mp *ip + Tprev * mt *it

    }

    def A(zeta: Double, l: Double, g: Double, M: Double,
          alpha: Double, p: Double, a: Double, e: Double, eta: Double,
          eps: Double, phi: Double,phi2: Double, delta: Double,
          mp: Double, mt: Double,ip: Double,it: Double, Cprev : Double, Aprev: Double, Tprev: Double): Double = {

      Aprev * g * ( 1 - (Aprev/ (1+ M/(1+eta*Tprev/(eps+1))))) - zeta*l*Aprev*Tprev - p*Aprev
    }

    def T(zeta: Double, l: Double, g: Double, M: Double,
          alpha: Double, p: Double, a: Double, e: Double, eta: Double,
          eps: Double, phi: Double,phi2: Double, delta: Double,
          mp: Double, mt: Double,ip: Double,it: Double, Cprev : Double, Aprev: Double, Tprev: Double): Double = {

      Tprev *(- alpha*Tprev + a*zeta*Aprev/(Aprev + phi) + e*Cprev/(Cprev+phi2*Tprev+phi2)  )
    }

    val h= 0.00001

    val k1_C = C(zeta, l, g , M  , alpha , p , a , e , eta , eps
      , phi, phi2  , delta , mp , mt , ip, it, Cprev  , Aprev , Tprev )

    val k1_A = A(zeta, l, g , M , alpha , p , a , e , eta , eps
      , phi, phi2  , delta , mp , mt , ip, it, Cprev  , Aprev , Tprev )

    val k1_T = T(zeta, l, g , M , alpha , p , a , e , eta , eps
      , phi, phi2  , delta , mp , mt , ip, it, Cprev  , Aprev , Tprev )



    val k2_C = C(zeta, l, g , M , alpha , p , a , e , eta , eps
      , phi  , phi2, delta , mp , mt ,ip,it, Cprev + (h/2)*k1_C  , Aprev + (h/2)*k1_A , Tprev + (h/2)*k1_T )

    val k2_A = A(zeta, l, g , M , alpha , p , a , e , eta , eps
      , phi , phi2, delta , mp , mt ,ip,it, Cprev + (h/2)*k1_C  , Aprev + (h/2)*k1_A , Tprev + (h/2)*k1_T )

    val k2_T = T(zeta, l, g , M  , alpha , p , a , e , eta , eps
      , phi  , phi2, delta , mp , mt ,ip,it, Cprev + (h/2)*k1_C  , Aprev + (h/2)*k1_A , Tprev + (h/2)*k1_T )



    val k3_C = C(zeta, l, g , M  , alpha , p , a , e , eta , eps
      , phi , phi2 , delta , mp , mt ,ip,it, Cprev + (h/2)*k2_C  , Aprev + (h/2)*k2_A , Tprev + (h/2)*k2_T )

    val k3_A = A(zeta, l, g , M  , alpha , p , a , e , eta , eps
      , phi , phi2 , delta , mp , mt ,ip,it, Cprev + (h/2)*k2_C  , Aprev + (h/2)*k2_A , Tprev + (h/2)*k2_T )

    val k3_T = T(zeta, l, g , M, alpha , p , a , e , eta , eps
      , phi , phi2 , delta , mp , mt ,ip,it, Cprev + (h/2)*k2_C  , Aprev + (h/2)*k2_A , Tprev + (h/2)*k2_T )



    val k4_C = C(zeta, l, g , M, alpha , p , a , e , eta , eps
      , phi , phi2 , delta , mp , mt ,ip,it, Cprev + h*k3_C  , Aprev + h*k3_A , Tprev + h*k3_T )

    val k4_A = A(zeta, l, g , M , alpha , p , a , e , eta , eps
      , phi , phi2 , delta , mp , mt ,ip,it, Cprev + h*k3_C  , Aprev + h*k3_A , Tprev + h*k3_T )

    val k4_T = T(zeta, l, g , M , alpha , p , a , e , eta , eps
      , phi , phi2 , delta , mp , mt ,ip,it, Cprev + h*k3_C  , Aprev + h*k3_A , Tprev + h*k3_T )



    val Cnew = Cprev + h/6 *(k1_C + 2*k2_C + 2*k3_C + k4_C)

    val Anew = Aprev + h/6 *(k1_A + 2*k2_A + 2*k3_A + k4_A)

    val Tnew = Tprev + h/6 *(k1_T + 2*k2_T + 2*k3_T + k4_T)

    val addA = h/6 * (k1_A + 2*k2_A + 2*k3_A + k4_A)
    val addT = h/6 *(k1_T + 2*k2_T + 2*k3_T + k4_T)

    //   println(s"Aprev : $Aprev,  A new :$Anew, add : $addA, k1 : $k1_A,k2 : $k2_A,k3 : $k3_A k4 : $k4_A")
    //  println(s"Tprev : $Tprev,  T new :$Tnew, add : $addT, k1 : $k1_T,k2 : $k2_T,k3 : $k3_T k4 : $k4_T")
    Array(Cnew, Anew, Tnew)

  }

}
