package com.codebetyars.skyhussars.engine.physics

import com.jme3.math.Vector3f

import scala.beans.BeanProperty
//remove if not needed

class Engine(private var engineLocation: EngineLocation) extends ThrustProducer with RigidBody {

  @BeanProperty
  var centerOfGravity: Vector3f = engineLocation.getLocation

  private var vMaxThrust: Vector3f = new Vector3f(0, 0, engineLocation.getEngineDescriptor.getThrustMax)

  private var throttle: Float = 0.0f

  override def getThrust(): Vector3f = vMaxThrust.mult(throttle)

  override def setThrottle(throttle: Float) {
    this.throttle = throttle
  }
}
