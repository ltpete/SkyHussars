package com.codebetyars.skyhussars.engine.physics

import com.jme3.math.Vector3f
import com.jme3.scene.Spatial
//remove if not needed

trait PlanePhysics {

  def update(tpf: Float, model: Spatial): Unit

  def setElevator(elevator: Float): Unit

  def getInfo(): String

  def setThrust(thrust: Float): Unit

  def setSpeedForward(model: Spatial, kmh: Float): Unit

  def setAileron(aileron: Float): Unit

  def setRudder(rudder: Float): Unit

  def getVVelovity(): Vector3f

  def getSpeedKmH(): String
}
