package com.codebetyars.skyhussars.engine.data

import java.util.List

import scala.beans.BeanProperty
//remove if not needed

class ArmamentGroup {

  @BeanProperty
  var name: String = _

  @BeanProperty
  var armaments: List[Armament] = _
}
