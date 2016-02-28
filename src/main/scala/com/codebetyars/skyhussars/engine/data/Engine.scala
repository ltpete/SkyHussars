package com.codebetyars.skyhussars.engine.data

import scala.beans.{BeanProperty, BooleanBeanProperty}
//remove if not needed

class Engine {

  @BeanProperty
  var name: String = _

  private var sound: String = _

  @BeanProperty
  var thrustMax: Float = _

  @BeanProperty
  var thrustMin: Float = _

  @BooleanBeanProperty
  var hasAfterburner: Boolean = _

  @BeanProperty
  var afterBurnerThrustMax: Float = _
}
