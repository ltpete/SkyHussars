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

  override def update(tpf: Float): GameState = {
    var nextState: GameState = this
    if (guiManager.gameStarted) {
      nextState = pendingMission
      guiManager.startGame()
    }
    nextState
  }

  override def close() {
  }

  override def initialize() {
    guiManager.stopGame()
    guiManager.cursor(true)
  }

  def startGame() {
    guiManager.startGame()
  }

  def bind(nifty: Nifty, screen: Screen) {
  }

  def onStartScreen() {
  }

  def onEndScreen() {
  }
}
