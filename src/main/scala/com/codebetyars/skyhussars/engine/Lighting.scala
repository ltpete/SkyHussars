package com.codebetyars.skyhussars.engine

import java.util.{LinkedList, List}

import com.codebetyars.skyhussars.utils.Logging
import com.jme3.light.{AmbientLight, DirectionalLight, Light, PointLight}
import com.jme3.math.{ColorRGBA, FastMath, Vector3f}
import jme3utilities.sky.SkyControl
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.beans.BeanProperty

@Component
class Lighting extends InitializingBean with Logging {

  @Autowired
  private var skyControl: SkyControl = _

  @BeanProperty
  var lights: List[Light] = _

  private var directionalLight: DirectionalLight = _

  private var ambientLight: AmbientLight = _

  private var pointLight: PointLight = _

  override def afterPropertiesSet() {
    directionalLight = new DirectionalLight()
    directionalLight.setColor(ColorRGBA.White.mult(0.5f))
    directionalLight.setDirection(new Vector3f(0.0f, -1.0f, 0.0f))
    ambientLight = new AmbientLight()
    ambientLight.setColor(ColorRGBA.White.mult(0.5f))
    pointLight = new PointLight()
    lights = new LinkedList()
    lights.add(directionalLight)
    lights.add(ambientLight)
    setLightingBodies(skyControl.getSunAndStars.getSunDirection, skyControl.getMoonDirection)
  }

  def setLightingBodies(sun: Vector3f, moon: Vector3f) {
    val sunAt = sun.angleBetween(Vector3f.UNIT_Y)
    val moonAt = moon.angleBetween(Vector3f.UNIT_Y)
    logger.debug("Sun at: " + sunAt + ", moon at: " + moonAt)
    if (sunAt < FastMath.HALF_PI) {
      directionalLight.setDirection(sun.negate())
      val lightStrength = ColorRGBA.White.mult(1f - sunAt / 4f)
      directionalLight.setColor(lightStrength)
      ambientLight.setColor(lightStrength)
    } else if (moonAt < FastMath.HALF_PI) {
      directionalLight.setDirection(moon.negate())
      val lightStrength = ColorRGBA.White.mult(0.25f - moonAt / 6f)
      directionalLight.setColor(lightStrength)
      ambientLight.setColor(lightStrength)
    } else {
      directionalLight.setDirection(Vector3f.UNIT_Y.negate())
      val lightStrength = ColorRGBA.White.mult(0.24f)
      directionalLight.setColor(lightStrength)
      ambientLight.setColor(lightStrength)
    }
    logger.debug("Direction of light: " + directionalLight.getDirection)
  }
}
