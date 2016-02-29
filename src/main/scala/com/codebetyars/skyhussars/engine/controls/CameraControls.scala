package com.codebetyars.skyhussars.engine.controls

import ControlsMapper._
import com.codebetyars.skyhussars.engine.CameraManager
import com.jme3.input.controls.ActionListener

class CameraControls(private var cameraManager: CameraManager) extends ActionListener {

  override def onAction(name: String, isPressed: Boolean, tpf: Float) {
    name match {
      case CAMERA_INCREASE_FOV => cameraManager.setFovChangeActive(active = isPressed, fovNarrowing = false)
      case CAMERA_DECREASE_FOV => cameraManager.setFovChangeActive(active = isPressed, fovNarrowing = true)
    }
  }

}
