package com.codebetyars.skyhussars.engine.controls

import ControlsMapper._
import com.codebetyars.skyhussars.engine.Pilot
import com.jme3.input.controls.ActionListener

class FlightKeyboardControls(var pilot: Pilot) extends ActionListener {

  override def onAction(name: String, isPressed: Boolean, tpf: Float) {
    val value = if (isPressed) 1 else 0
    name match {
      case CONTROL_THROTTLE_CUT =>  pilot.setThrottle(0.0f)
      case CONTROL_THROTTLE_10 =>   pilot.setThrottle(0.1f)
      case CONTROL_THROTTLE_20 =>   pilot.setThrottle(0.2f)
      case CONTROL_THROTTLE_30 =>   pilot.setThrottle(0.3f)
      case CONTROL_THROTTLE_40 =>   pilot.setThrottle(0.4f)
      case CONTROL_THROTTLE_50 =>   pilot.setThrottle(0.5f)
      case CONTROL_THROTTLE_60 =>   pilot.setThrottle(0.6f)
      case CONTROL_THROTTLE_70 =>   pilot.setThrottle(0.7f)
      case CONTROL_THROTTLE_80 =>   pilot.setThrottle(0.8f)
      case CONTROL_THROTTLE_90 =>   pilot.setThrottle(0.9f)
      case CONTROL_THROTTLE_FULL => pilot.setThrottle(1.0f)

      case CONTROL_NOSE_DOWN => pilot.setElevator(-value)
      case CONTROL_NOSE_UP => pilot.setElevator(+value)

      case CONTROL_ROTATE_LEFT => pilot.setAileron(-value)
      case CONTROL_ROTATE_RIGHT => pilot.setAileron(+value)

      case CONTROL_RUDDER_LEFT => pilot.setRudder(-value)
      case CONTROL_RUDDER_RIGHT => pilot.setRudder(+value)

      case CONTROL_FIRE         => pilot.firing(isPressed)
    }
  }

}
