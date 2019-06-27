package simpark.app

import scala.math.{pow, sqrt}


class Variables(val Amin : Double, val Amax : Double, val Cmin : Double, val Cmax : Double, val Tmin : Double,
                val Tmax : Double, val emin : Double, val emax : Double, val zmin : Double, val zmax : Double ) {


  def file2variable(filename : String ): Variables = {
    println("c'est parti pour le file2variable")
    val name = """ParcRectangles_Cmin(\d+\.\d+)Cmax(\d+\.\d+)Amin(\d+\.\d+)Amax(\d+\.\d+)Tmin(\d+\.\d+)Tmax(\d+\.\d+)Emin(\d+\.\d+)Emax(\d+\.\d+)__depth(\d+)Zmin(\d+\.\d+)Zmax(\d+\.\d+)\.txt""".r
    filename match {
    case name(cmin, cmax, amin, amax, tmin, tmax, emi, ema, depth, zmi, zma) => {
      println("on a un new !")
      val variableFromFile = new Variables(amin.toDouble, amax.toDouble,cmin.toDouble, cmax.toDouble,  tmin.toDouble, tmax.toDouble, emi.toDouble, ema.toDouble, zmi.toDouble, zma.toDouble)
      variableFromFile
    }
    }
  }

  def distBetweenVariables(Var1 : Variables , Var2 : Variables) : Double = {
    println("c'est parti pour le calcul de distance")
    var dist = sqrt(pow((Var1.Amax - Var2.Amax),2) + pow((Var1.Amin - Var2.Amin),2) + pow((Var1.Cmin - Var2.Cmin),2) +
      pow((Var1.Cmax - Var2.Cmax),2) + pow((Var1.Tmax - Var2.Tmax),2) + pow((Var1.Tmin - Var2.Tmin),2)
      + pow((Var1.emax - Var2.emax),2) + pow((Var1.emin - Var2.emin),2) + pow((Var1.zmax - Var2.zmax),2) + pow((Var1.zmin - Var2.zmin),2) )
    dist
  }

}
