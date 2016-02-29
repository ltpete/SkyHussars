package com.codebetyars.skyhussars.engine

import java.text.SimpleDateFormat
import java.util.Date

import com.codebetyars.skyhussars.utils.Logging
import com.jme3.asset.AssetManager
import com.jme3.audio.AudioRenderer
import com.jme3.input.InputManager
import com.jme3.niftygui.NiftyJmeDisplay
import com.jme3.renderer.ViewPort
import de.lessvoid.nifty.controls.{DropDown, DropDownSelectionChangedEvent}
import de.lessvoid.nifty.elements.render.TextRenderer
import de.lessvoid.nifty.screen.{Screen, ScreenController}
import de.lessvoid.nifty.{Nifty, NiftyEventSubscriber}
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.beans.BooleanBeanProperty

@Component
class GuiManager extends ScreenController with InitializingBean with Logging {

  @Autowired
  var assetManager: AssetManager = _

  @Autowired
  var inputManager: InputManager = _

  @Autowired
  var audioRenderer: AudioRenderer = _

  @Autowired
  var guiViewPort: ViewPort = _

  @Autowired
  var cameraManager: CameraManager = _

  @Autowired
  var dayLightWeatherManager: DayLightWeatherManager = _

  private var nifty: Nifty = _

  private var niftyDisplay: NiftyJmeDisplay = _

  override def afterPropertiesSet() {
    niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort)
    nifty = niftyDisplay.getNifty
  }

  def createGUI() {
    nifty.fromXml("Interface/BasicGUI.xml", "start", this)
    nifty.addControls()
    nifty.update()
    val timeControl = nifty.getScreen("start").findNiftyControl("timeControl", classOf[DropDown[String]])
    timeControl.addItem("Now")
    for (i <- 0 until 24) {
      timeControl.addItem((if (i < 10) "0" + i else i) + ":00")
    }
    guiViewPort.addProcessor(niftyDisplay)
  }

  @NiftyEventSubscriber(id = "timeControl")
  def setTime(id: String, event: DropDownSelectionChangedEvent[String]) {
    val time = event.getSelection.asInstanceOf[String]
    val dateformat = new SimpleDateFormat("HH")
    var hour: Int = 0
    hour = if ("Now" == time) java.lang.Integer.parseInt(dateformat.format(new Date())) else java.lang.Integer.parseInt(time.split(":")(0))
    logger.debug("Set time of flight:" + hour)
    dayLightWeatherManager.setHour(hour)
  }

  def update(speed: String) {
    nifty.getCurrentScreen.findElementByName("speedDisplay").getRenderer(classOf[TextRenderer]).setText(speed + "km/h")
  }

  def switchScreen(screenId: String) {
    nifty.gotoScreen(screenId)
  }

  def cursor(cursor: Boolean) {
    inputManager.setCursorVisible(cursor)
    cameraManager.flyCamActive(cursor)
  }

  def cursor(): Boolean = inputManager.isCursorVisible

  def bind(nifty: Nifty, screen: Screen) {
  }

  def onStartScreen() {
  }

  def onEndScreen() {
  }

  def startGame() {
    setGameRunning(true)
  }

  @BooleanBeanProperty
  var gameRunning: Boolean = false

}
