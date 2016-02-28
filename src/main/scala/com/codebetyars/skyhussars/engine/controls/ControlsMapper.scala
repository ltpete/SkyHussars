package com.codebetyars.skyhussars.engine.controls

import com.codebetyars.skyhussars.engine.controls.ControlsMapper._
import com.codebetyars.skyhussars.utils.Logging
import com.jme3.input.KeyInput._
import com.jme3.input.controls._
import com.jme3.input.{InputManager, MouseInput}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

object ControlsMapper {
  val GAME_PAUSE = "Pause"
  val GAME_CAMERA = "Camera"
  val GAME_RESET = "Reset"

  val CAMERA_INCREASE_FOV = "IncreaseFov"
  val CAMERA_DECREASE_FOV = "DecreaseFov"

  val CONTROL_NOSE_UP = "NoseDown"
  val CONTROL_NOSE_DOWN = "NoseUp"
  val CONTROL_ROTATE_LEFT = "RotateLeft"
  val CONTROL_ROTATE_RIGHT = "RotateRight"
  val CONTROL_RUDDER_LEFT = "RudderLeft"
  val CONTROL_RUDDER_RIGHT = "RudderRight"
  val CONTROL_THROTTLE_CUT = "Throttle00"
  val CONTROL_THROTTLE_10 = "Throttle10"
  val CONTROL_THROTTLE_20 = "Throttle20"
  val CONTROL_THROTTLE_30 = "Throttle30"
  val CONTROL_THROTTLE_40 = "Throttle40"
  val CONTROL_THROTTLE_50 = "Throttle50"
  val CONTROL_THROTTLE_60 = "Throttle60"
  val CONTROL_THROTTLE_70 = "Throttle70"
  val CONTROL_THROTTLE_80 = "Throttle80"
  val CONTROL_THROTTLE_90 = "Throttle90"
  val CONTROL_THROTTLE_FULL = "Throttle100"

  val CONTROL_FIRE = "Fire"

}

@Component
class ControlsMapper extends Logging {

  @Autowired
  var inputManager: InputManager = _

  def register(listener: InputListener, map: Map[_ <: Trigger, String]) {
    map.foreach { case (trigger, action) =>
      inputManager.addMapping(action, trigger)
      inputManager.addListener(listener, action)
    }
  }

  def registerKeys(listener: InputListener, map: Map[Int, String]) {
    register(listener, map.map { case (key, action) => new KeyTrigger(key) -> action})
  }

  def setupFlowControls(actionListener: ActionListener) {
    registerKeys(actionListener, Map(
      KEY_P -> GAME_PAUSE,
      KEY_C -> GAME_CAMERA,
      KEY_R -> GAME_RESET
    ))
  }

  def setupFlightKeyboardControls(flightKeyboardControls: FlightKeyboardControls) {
    registerKeys(flightKeyboardControls, Map(
      KEY_UP    -> CONTROL_NOSE_UP,
      KEY_DOWN  -> CONTROL_NOSE_DOWN,
      KEY_LEFT  -> CONTROL_ROTATE_LEFT,
      KEY_RIGHT -> CONTROL_ROTATE_RIGHT,
      KEY_W     -> CONTROL_NOSE_UP,
      KEY_S     -> CONTROL_NOSE_DOWN,
      KEY_A     -> CONTROL_ROTATE_LEFT,
      KEY_D     -> CONTROL_ROTATE_RIGHT,
      KEY_Q     -> CONTROL_RUDDER_LEFT,
      KEY_E     -> CONTROL_RUDDER_RIGHT,
      KEY_X     -> CONTROL_THROTTLE_CUT,
      KEY_1     -> CONTROL_THROTTLE_10,
      KEY_2     -> CONTROL_THROTTLE_20,
      KEY_3     -> CONTROL_THROTTLE_30,
      KEY_4     -> CONTROL_THROTTLE_40,
      KEY_5     -> CONTROL_THROTTLE_50,
      KEY_6     -> CONTROL_THROTTLE_60,
      KEY_7     -> CONTROL_THROTTLE_70,
      KEY_8     -> CONTROL_THROTTLE_80,
      KEY_9     -> CONTROL_THROTTLE_90,
      KEY_0     -> CONTROL_THROTTLE_FULL,
      KEY_SPACE -> CONTROL_FIRE
    ))
  }

  def setupCameraControls(cameraControls: CameraControls) {
    register(cameraControls, Map(
      new MouseButtonTrigger(MouseInput.BUTTON_LEFT) -> CAMERA_INCREASE_FOV,
      new MouseButtonTrigger(MouseInput.BUTTON_RIGHT) -> CAMERA_DECREASE_FOV
    ))
  }

}
