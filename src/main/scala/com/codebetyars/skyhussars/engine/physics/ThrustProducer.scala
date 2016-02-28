package com.codebetyars.skyhussars.engine.physics

import com.jme3.math.Vector3f
//remove if not needed

trait ThrustProducer {

  def getThrust(): Vector3f

  def setThrottle(throttle: Float): Unit
}
