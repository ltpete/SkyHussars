package com.codebetyars.skyhussars

import java.util.{Calendar, GregorianCalendar}

import com.codebetyars.skyhussars.engine.{CameraManager, GameState, GuiManager, MainMenu}
import com.codebetyars.skyhussars.engine.mission.MissionFactory
import com.jme3.asset.AssetManager
import com.jme3.math.FastMath
import com.jme3.renderer.{Camera, RenderManager}
import com.jme3.scene.Node
import jme3utilities.sky.SkyControl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
//remove if not needed

@Configuration
@ComponentScan
class SkyHussarsContext {

  @Autowired
  private var assetManager: AssetManager = _

  @Autowired
  private var camera: Camera = _

  @Autowired
  private var rootNode: Node = _

  @Autowired
  private var cameraManager: CameraManager = _

  @Autowired
  private var guiManager: GuiManager = _

  @Autowired
  private var mainMenu: MainMenu = _

  @Autowired
  private var missionFactory: MissionFactory = _

  private var gameState: GameState = _

  @Bean
  def skyControl(): SkyControl = {
    val now = new GregorianCalendar()
    val skyControl = new SkyControl(assetManager, camera, 0.9f, true, true)
    skyControl.getSunAndStars.setHour(now.get(Calendar.HOUR_OF_DAY))
    skyControl.getSunAndStars.setSolarLongitude(now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
    skyControl.getSunAndStars.setObserverLatitude(37.4046f * FastMath.DEG_TO_RAD)
    skyControl.setCloudiness(0f)
    rootNode.addControl(skyControl)
    skyControl.setEnabled(true)
    skyControl
  }

  def simpleInitApp() {
    cameraManager.initializeCamera()
    guiManager.createGUI()
    mainMenu.setPendingMission(missionFactory.createMission("Test mission"))
    mainMenu.initialize()
    gameState = mainMenu
  }

  def simpleUpdate(tpf: Float) {
    val nextState = gameState.update(tpf)
    if (nextState != gameState) {
      gameState.close()
      gameState = nextState
      gameState.initialize()
    }
  }

  def simpleRender(rm: RenderManager) {
  }
}
