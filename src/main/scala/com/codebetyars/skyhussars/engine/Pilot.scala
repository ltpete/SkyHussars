package com.codebetyars.skyhussars.engine

import com.codebetyars.skyhussars.engine.plane.Plane

class Pilot(var plane: Plane) {

  def setThrottle(throttle: Float) {
    plane.setThrottle(throttle)
  }

  def firing(firing: Boolean) {
    plane.setFiring(firing)
  }

  def setAileron(aileron: Float) {
    plane.setAileron(aileron)
  }

  def setElevator(elevator: Float) {
    plane.setElevator(elevator)
  }

  def setRudder(rudder: Float): Unit = {
    plane.setRudder(rudder)
  }
}
