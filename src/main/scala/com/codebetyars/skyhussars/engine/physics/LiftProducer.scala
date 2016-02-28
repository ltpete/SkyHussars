package com.codebetyars.skyhussars.engine.physics

import com.jme3.math.{Quaternion, Vector3f}
//remove if not needed

trait LiftProducer {

  def calculateResultantForce(airDensity: Float, 
      vVelocity: Vector3f, 
      situation: Quaternion, 
      angularVelocity: Vector3f): Vector3f
}
