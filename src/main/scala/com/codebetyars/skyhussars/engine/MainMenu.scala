package com.codebetyars.skyhussars.engine

import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.screen.{Screen, ScreenController}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.beans.BeanProperty
//remove if not needed

@Component
class MainMenu extends GameState with ScreenController {

  @Autowired
  private var guiManager: GuiManager = _

  @BeanProperty
  var pendingMission: GameState = _

  private var time: Float = 0

  def update(tpf: Float): GameState = {
    if (guiManager.isGameRunning) pendingMission else this
  }

  def initialize() {
    guiManager.setGameRunning(false)
    guiManager.cursor(true)
  }

  def startGame() {
    guiManager.setGameRunning(true)
  }

  def bind(nifty: Nifty, screen: Screen) {
  }

  def onStartScreen() {
  }

  def onEndScreen() {
  }

  def close() {
  }

}
