package com.codebetyars.skyhussars.engine.controls

import com.codebetyars.skyhussars.engine.controls.ControlsMapper._
import com.codebetyars.skyhussars.engine.mission.Mission
import com.jme3.input.controls.ActionListener

class FlowControls(var game: Mission) extends ActionListener {

  override def onAction(name: String, isPressed: Boolean, tpf: Float) {
    if (isPressed) name match {
      case GAME_PAUSE => game.paused = !game.paused
      case GAME_CAMERA =>
      case GAME_RESET =>
    }
  }

}
