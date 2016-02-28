package com.codebetyars.skyhussars.engine.controls

import com.codebetyars.skyhussars.engine.Pilot
import com.jme3.input.controls.ActionListener

class FlightKeyboardControls(var pilot: Pilot) extends ActionListener {

  override def onAction(name: String, isPressed: Boolean, tpf: Float) {
    val value = if (isPressed) 1 else 0
    name match {
      case "Throttle0%" => pilot.setThrottle(0)
      case "Throttle20%" => pilot.setThrottle(0.2f)
      case "Throttle40%" => pilot.setThrottle(0.4f)
      case "Throttle60%" => pilot.setThrottle(0.6f)
      case "Throttle80%" => pilot.setThrottle(0.8f)
      case "Throttle100%" => pilot.setThrottle(1.0f)
      case "NoseDown" => pilot.setElevator(-value)
      case "NoseUp" => pilot.setElevator(+value)
      case "RotateLeft" => pilot.setAileron(-value)
      case "RotateRight" => pilot.setAileron(+value)
      case "RudderLeft" => pilot.setRudder(-value)
      case "RudderRight" => pilot.setRudder(+value)
      case "Fire" => pilot.firing(isPressed)
    }
  }

}
