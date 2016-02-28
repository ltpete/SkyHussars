package com.codebetyars.skyhussars.engine.mission

import com.codebetyars.skyhussars.engine._
import com.codebetyars.skyhussars.engine.plane.Plane
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager

class Mission(var planes: List[Plane],
              var projectileManager: ProjectileManager,
              var soundManager: SoundManager,
              var cameraManager: CameraManager,
              var terrainManager: TerrainManager,
              var guiManager: GuiManager,
              var dayLightWeatherManager: DayLightWeatherManager) extends GameState {

  val player = new Pilot(planes.find(_.planeMission.isPlayer).get)

  var paused: Boolean = false

  var ended: Boolean = false

  def update(tpf: Float) = {
    if (!paused && !ended) {
      for (plane <- planes) {
        plane.update(tpf)
        plane.setCrashed(terrainManager.checkCollisionWithGround(plane))
      }

      projectileManager.update(tpf)
      ended = player.plane.getCrashed

      guiManager.update(player.plane.getSpeedKmH)
    } else {
      soundManager.muteAllSounds()
    }
    cameraManager.update(tpf)
    this
  }

  def initialize() {
    cameraManager.moveCameraTo(player.plane.getLocation)
    cameraManager.followWithCamera(player.plane.node)
    cameraManager.init()

    guiManager.switchScreen("main")
    guiManager.cursor(false)

    ended = false
  }

  def close() {
  }

}
