package com.codebetyars.skyhussars.engine.mission

import com.codebetyars.skyhussars.engine._
import com.codebetyars.skyhussars.engine.plane.Plane
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager

class Mission(val planes: List[Plane],
              val projectileManager: ProjectileManager,
              val soundManager: SoundManager,
              val cameraManager: CameraManager,
              val terrainManager: TerrainManager,
              val guiManager: GuiManager,
              val dayLightWeatherManager: DayLightWeatherManager) extends GameState {

  val player = new Pilot(planes.find(_.planeMission.isPlayer).get)
  var paused: Boolean = false
  var ended: Boolean = false

  initializeCamera()

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

  def initializeCamera(): Unit = {
    cameraManager.moveCameraTo(player.plane.getLocation)
    cameraManager.followWithCamera(player.plane.node)
    cameraManager.init()
  }

  def initialize() {
    guiManager.switchScreen("main")
    guiManager.cursor(false)

    initializeCamera()

    ended = false
  }

  def close() {
  }

}
