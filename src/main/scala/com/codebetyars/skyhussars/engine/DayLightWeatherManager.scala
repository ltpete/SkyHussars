package com.codebetyars.skyhussars.engine

import com.jme3.scene.Node
import jme3utilities.sky.SkyControl
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
//remove if not needed
import scala.collection.JavaConversions._

@Component
class DayLightWeatherManager extends InitializingBean {

  @Autowired
  private var rootNode: Node = _

  @Autowired
  private var skyControl: SkyControl = _

  @Autowired
  private var lighting: Lighting = _

  override def afterPropertiesSet() {
    for (light <- lighting.getLights) {
      rootNode.addLight(light)
    }
  }

  def setHour(hour: Int) {
    skyControl.getSunAndStars.setHour(hour)
    lighting.setLightingBodies(skyControl.getSunAndStars.getSunDirection, skyControl.getMoonDirection)
  }
}
