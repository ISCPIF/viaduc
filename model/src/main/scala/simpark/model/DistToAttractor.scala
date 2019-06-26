package simpark.model
import simpark.model.Utils.compute_CAT_RK4

object DistToAttractor {

  def apply(params: Vector[Double], CondInit: Vector[Double], indiceMax: Int): (Int, List[Double]) = {

    /*zeta: Double, l: Double, g: Double, M: Double,
    alpha: Double, p: Double, a: Double, e: Double, eta: Double,
    eps: Double, phi: Double, phi2: Double, d: Double, delta: Double,
    mp: Double, mt: Double,ip: Double,it: Double*/

    val zeta = params(0)
    val l = params(1)
    val g = params(2)
    val M = params(3)
    val alpha = params(4)
    val p = params(5)
    val a = params(6)
    val e = params(7)
    val eta = params(8)
    val eps = params(9)
    val phi = params(10)
    val phi2 = params(11)
    val delta = params(12)
    val mp = params(13)
    val mt = params(14)
    val ip = params(15)
    val it = params(16)


    val CAT_Init = compute_CAT_RK4(zeta, l, g, M, alpha, p, a, e, eta, eps, phi, phi2, delta, mp,
      mt, ip, it, CondInit(0), CondInit(1), CondInit(2))

    var Cprev = CAT_Init(0)
    var Aprev = CAT_Init(1)
    var Tprev = CAT_Init(2)

    var indice = 0
    var attractorFound = 0
    //println(indiceMax)

    while ((indice <= indiceMax)&&(attractorFound==0)) {
      // println(indice)

      val CAT = compute_CAT_RK4(zeta, l, g, M, alpha, p, a, e, eta, eps, phi, phi2, delta, mp,
        mt, ip, it, Cprev, Aprev, Tprev)

      var C = CAT(0)
      var A = CAT(1)
      var T = CAT(2)

      if ((C <= Cprev+0.01)&&(C >= Cprev-0.01)&&(A <= Aprev+0.01)&&(A >= Aprev-0.01)&&(T <= Tprev+0.01)&&(T >= Tprev-0.01)) {
        attractorFound = 1
      }

      indice = indice + 1
      Cprev = C
      Aprev = A
      Tprev = T
    }

    val tmp = List(Cprev, Aprev, Tprev)

    (indice, tmp)

  }



}

