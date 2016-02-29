package com.codebetyars.skyhussars.engine

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
  var skyControl: SkyControl = _

  val directionalLight = new DirectionalLight
  directionalLight.setColor(ColorRGBA.White.mult(0.5f))
  directionalLight.setDirection(new Vector3f(0.0f, -1.0f, 0.0f))

  val ambientLight = new AmbientLight
  ambientLight.setColor(ColorRGBA.White.mult(0.5f))

  val pointLight = new PointLight

  val lights = List(directionalLight, ambientLight)

  override def afterPropertiesSet(): Unit = {
    setLightingBodies(skyControl.getSunAndStars.getSunDirection, skyControl.getMoonDirection)
  }

  def setLightingBodies(sun: Vector3f, moon: Vector3f) {
    val sunAt = sun.angleBetween(Vector3f.UNIT_Y)
    val moonAt = moon.angleBetween(Vector3f.UNIT_Y)

    directionalLight.setColor(ColorRGBA.White.mult(0.5f))
    directionalLight.setDirection(new Vector3f(0.0f, -1.0f, 0.0f))
    ambientLight.setColor(ColorRGBA.White.mult(0.5f))

    if (sunAt < FastMath.HALF_PI) {
      val lightStrength = ColorRGBA.White.mult(1f - sunAt / 4f)
      directionalLight.setDirection(sun.negate())
      directionalLight.setColor(lightStrength)
      ambientLight.setColor(lightStrength)
    } else if (moonAt < FastMath.HALF_PI) {
      val lightStrength = ColorRGBA.White.mult(0.25f - moonAt / 6f)
      directionalLight.setDirection(moon.negate())
      directionalLight.setColor(lightStrength)
      ambientLight.setColor(lightStrength)
    } else {
      val lightStrength = ColorRGBA.White.mult(0.24f)
      directionalLight.setDirection(Vector3f.UNIT_Y.negate())
      directionalLight.setColor(lightStrength)
      ambientLight.setColor(lightStrength)
    }

    logger.debug("Sun at: " + sunAt + ", moon at: " + moonAt)
    logger.debug("Direction of light: " + directionalLight.getDirection)
  }

}
