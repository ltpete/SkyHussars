package com.codebetyars.skyhussars.engine

import com.jme3.scene.Node
import jme3utilities.sky.SkyControl
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DayLightWeatherManager extends InitializingBean {

  @Autowired
  var rootNode: Node = _

  @Autowired
  var skyControl: SkyControl = _

  @Autowired
  var lighting: Lighting = _

  def afterPropertiesSet() {
    lighting.lights.foreach(rootNode.addLight)
  }

  def setHour(hour: Int) {
    skyControl.getSunAndStars.setHour(hour)
    lighting.setLightingBodies(skyControl.getSunAndStars.getSunDirection, skyControl.getMoonDirection)
  }

}
