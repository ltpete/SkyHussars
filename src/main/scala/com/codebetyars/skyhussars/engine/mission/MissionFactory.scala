package com.codebetyars.skyhussars.engine.mission

import com.codebetyars.skyhussars.SkyHussarsDataModel._
import com.codebetyars.skyhussars.engine._
import com.codebetyars.skyhussars.engine.controls.{ControlsManager, ControlsMapper}
import com.codebetyars.skyhussars.engine.plane.{Plane, PlaneFactory}
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MissionFactory {

  @Autowired var projectileManager: ProjectileManager = _
  @Autowired var soundManager: SoundManager = _
  @Autowired var controlsMapper: ControlsMapper = _
  @Autowired var cameraManager: CameraManager = _
  @Autowired var terrainManager: TerrainManager = _
  @Autowired var guiManager: GuiManager = _
  @Autowired var planeFactory: PlaneFactory = _
  @Autowired var dayLightWeatherManager: DayLightWeatherManager = _

  def createMission(missionName: String): Mission = {
    val missionDescriptor = Missions(missionName)
    val planes = missionDescriptor.planes.map(planeFactory.createPlane)
    val mission = new Mission(planes, projectileManager, soundManager, cameraManager, terrainManager, guiManager, dayLightWeatherManager)
    ControlsManager.setupControl(controlsMapper, mission, cameraManager)

    mission
  }

}
