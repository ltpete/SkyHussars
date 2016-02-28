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

  var player: Pilot = _

  var paused: Boolean = false

  private var ended: Boolean = false

  private var pilots: List[Pilot] = _

  for (plane <- planes if plane.planeMissionDescriptor.player) {
    player = new Pilot(plane)
  }

  initiliazePlayer()

  def initializeScene() {
    initiliazePlayer()
    ended = false
  }

  private def initiliazePlayer() {
    cameraManager.moveCameraTo(player.plane.getLocation)
    cameraManager.followWithCamera(player.plane.getNode)
    cameraManager.init()
  }

  override def update(tpf: Float): GameState = {
    if (!paused && !ended) {
      for (plane <- planes) {
        plane.update(tpf)
        if (terrainManager.checkCollisionWithGround(plane)) {
          plane.crashed(true)
        }
      }
      projectileManager.update(tpf)
      if (player.plane.crashed) {
        ended = true
      }
      guiManager.update(player.plane.getSpeedKmH)
    } else {
      soundManager.muteAllSounds()
    }
    cameraManager.update(tpf)
    this
  }

  override def close() {
  }

  override def initialize() {
    initializeScene()
    guiManager.switchScreen("main")
    guiManager.cursor(false)
    ended = false
  }

}
