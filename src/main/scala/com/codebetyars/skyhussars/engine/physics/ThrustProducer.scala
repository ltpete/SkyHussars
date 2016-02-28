package com.codebetyars.skyhussars.engine.physics

import com.jme3.math.Vector3f

trait ThrustProducer {

  def getThrust: Vector3f

  def setThrottle(throttle: Float)

}
