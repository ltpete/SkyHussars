package com.codebetyars.skyhussars.engine.physics

import com.codebetyars.skyhussars.SkyHussarsDataModel.EngineLocation
import com.jme3.math.Vector3f

import scala.beans.BeanProperty

class Engine(engineLocation: EngineLocation) extends ThrustProducer with RigidBody {

  @BeanProperty
  var throttle: Float = 0.0f

  val vMaxThrust= new Vector3f(0, 0, engineLocation.engine.thrustMax)

  def getThrust = vMaxThrust.mult(throttle)

  def getCenterOfGravity: Vector3f = engineLocation.location

}
