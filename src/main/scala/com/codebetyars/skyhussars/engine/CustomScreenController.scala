package com.codebetyars.skyhussars.engine

import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.screen.{Screen, ScreenController}
//remove if not needed

class CustomScreenController extends ScreenController {

  private var speed: String = _

  def setSpeed(speed: String) {
    this.speed = speed
  }

  def bind(nifty: Nifty, screen: Screen) {
  }

  def onStartScreen() {
  }

  def onEndScreen() {
  }

  def getSpeed(): String = {
    val speedStr = ""
    speedStr + speed
  }
}
