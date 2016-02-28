package com.codebetyars.skyhussars.engine.physics

//remove if not needed

object WorldPhysicsData {

  private var airDensityAltitude: Array[Int] = Array(-1000, 0, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 15000, 20000, 25000, 30000, 40000, 50000, 60000, 70000, 80000)

  private var airDensity: Array[Float] = Array(1.347f, 1.225f, 1.112f, 1.007f, 0.9093f, 0.8194f, 0.7364f, 0.6601f, 0.59f, 0.5258f, 0.4671f, 0.4135f, 0.1948f, 0.08891f, 0.04008f, 0.01841f, 0.003996f, 0.001027f, 0.0003097f, 0.00008283f, 0.00001846f)

  def getAirDensity(altitude: Int): Float = {
    var index = 0
    for (altitudeIndicator <- airDensityAltitude) {
      if (altitudeIndicator > altitude) //break
      index += 1
    }
    airDensity(index)
  }
}
