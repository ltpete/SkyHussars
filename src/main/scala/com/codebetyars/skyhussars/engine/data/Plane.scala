package com.codebetyars.skyhussars.engine.data

import java.util.List

import scala.beans.BeanProperty
//remove if not needed

class Plane {

  @BeanProperty
  var name: String = _

  @BeanProperty
  var armamentGroups: List[ArmamentGroup] = _

  @BeanProperty
  var gun: GunDescriptor = _

  @BeanProperty
  var engine: Engine = _
}
