package com.codebetyars.skyhussars.engine.plane

import com.codebetyars.skyhussars.SkyHussarsDataModel._
import com.codebetyars.skyhussars.engine.physics.{AdvancedPlanePhysics, PlanePhysics}
import com.codebetyars.skyhussars.engine.weapons.{Bomb, Missile, ProjectileManager}
import com.codebetyars.skyhussars.utils.Logging
import com.jme3.audio.AudioNode
import com.jme3.math.{FastMath, Vector2f, Vector3f}
import com.jme3.scene.{Node, Spatial}

import scala.beans.BeanProperty

class Plane(model: Spatial, val planeMission: PlaneMissionDescriptor, engineSound: AudioNode, gunSound: AudioNode, projectileManager: ProjectileManager) extends Logging {

  val node: Node = new Node()

  val planeDescriptor = planeMission.plane

  val physics: PlanePhysics = new AdvancedPlanePhysics(this)

  val gunGroups = planeDescriptor.guns.map(new GunGroup(_, projectileManager))

  val missiles = List[Missile]() // not supported yet

  val bombs = List[Bomb]() // not supported yet

  var mass = planeDescriptor.massGross

  @BeanProperty
  var firing: Boolean = false

  @BeanProperty
  var crashed: Boolean = false

  this.physics.setSpeedForward(model, planeMission.initialSpeed)

  node.attachChild(model)
  node.attachChild(engineSound)
  node.attachChild(gunSound)

  def update(tpf: Float) {
    if (!crashed) {
      engineSound.play()

      physics.update(node, tpf)
      logger.debug(physics.getInfo)

      if (firing) {
        gunSound.play()
        gunGroups.foreach { guns =>
          guns.fire(node.getLocalTranslation, physics.getVVelovity, node.getWorldRotation)
        }
      } else {
        gunSound.stop()
      }

    } else {
      engineSound.stop()
      gunSound.stop()
    }
  }

  def setThrottle(throttle: Float) {
    val thrust = FastMath.clamp(throttle, 0.0f, 1.0f)
    physics.setThrust(thrust)
    engineSound.setPitch(0.5f + thrust)
  }

  def setAileron(aileron: Float) {
    physics.setAileron(aileron)
  }

  def setElevator(elevator: Float) {
    physics.setElevator(elevator)
  }

  def setRudder(rudder: Float) {
    physics.setRudder(rudder)
  }

  def setHeight(height: Int) {
    node.getLocalTranslation.setY(height)
  }

  def setLocation(x: Int, z: Int) {
    node.move(x, node.getLocalTranslation.y, z)
  }

  def setLocation(location: Vector3f) {
    node.move(location)
  }

  def getHeight = node.getLocalTranslation.y

  def getLocation = node.getLocalTranslation

  def getLocation2D = new Vector2f(node.getLocalTranslation.x, node.getLocalTranslation.z)

  def getSpeedKmH = physics.getSpeedKmH

}
