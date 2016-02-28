package com.codebetyars.skyhussars.engine.controls

import com.codebetyars.skyhussars.engine.CameraManager
import com.codebetyars.skyhussars.engine.mission.Mission

object ControlsManager {

  def setupControl(controlsMapper: ControlsMapper, mission: Mission, cameraManager: CameraManager) {
    controlsMapper.setupFlightKeyboardControls(new FlightKeyboardControls(mission.player))
    controlsMapper.setupFlowControls(new FlowControls(mission))
    controlsMapper.setupCameraControls(new CameraControls(cameraManager))
  }

}
