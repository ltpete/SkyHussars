package com.codebetyars.skyhussars.engine.physics

object WorldPhysicsData {

  private val AirDensity = Map(
    -1000 -> 1.347f,
        0 -> 1.225f,
     1000 -> 1.112f,
     2000 -> 1.007f,
     3000 -> 0.9093f,
     4000 -> 0.8194f,
     5000 -> 0.7364f,
     6000 -> 0.6601f,
     7000 -> 0.5900f,
     8000 -> 0.5258f,
     9000 -> 0.4671f,
    10000 -> 0.4135f,
    15000 -> 0.1948f,
    20000 -> 0.08891f,
    25000 -> 0.04008f,
    30000 -> 0.01841f,
    40000 -> 0.003996f,
    50000 -> 0.001027f,
    60000 -> 0.0003097f,
    70000 -> 0.00008283f,
    80000 -> 0.00001846f
  )

  def AirDensity(altitude: Float): Float = AirDensity.find(_._1 > altitude).map(_._2).getOrElse(0f)

}
