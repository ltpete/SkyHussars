package com.codebetyars.skyhussars.engine.plane

import com.codebetyars.skyhussars.SkyHussarsDataModel._
import com.codebetyars.skyhussars.engine.data.Engine
import com.codebetyars.skyhussars.engine.physics.{AdvancedPlanePhysics, PlanePhysics}
import com.codebetyars.skyhussars.engine.weapons.{Bomb, Gun, Missile, ProjectileManager}
import com.codebetyars.skyhussars.utils.Logging
import com.jme3.audio.AudioNode
import com.jme3.math.{FastMath, Vector2f, Vector3f}
import com.jme3.scene.{Node, Spatial}

class Plane(model: Spatial, planeMission: PlaneMissionDescriptor, gunSound: AudioNode, engineSound: AudioNode, projectileManager: ProjectileManager) extends Logging {

  val node: Node = new Node()

  val physics: PlanePhysics = new AdvancedPlanePhysics(planeMission.plane)

  private var guns: List[Gun] = _

  private var gunGroups: List[GunGroup] = _

  private var missiles: List[Missile] = _

  private var bombs: List[Bomb] = _

  private var engines: List[Engine] = _

  private var firing: Boolean = false

  var crashed: Boolean = false

  def updatePlanePhysics(tpf: Float) {
    physics.update(tpf, node)
    // pass the function, not calling it if not required
    logger.debug(physics.getInfo() _)
  }

  var accG: Vector3f = new Vector3f(0f, -9.81f, 0f)

  this.model.rotate(0, 0, 0 * FastMath.DEG_TO_RAD)

  this.physics.setSpeedForward(model, planeMission.initialSpeed)

  initializeGunGroup()

  node.attachChild(model)
  node.attachChild(engineSound)
  node.attachChild(gunSound)


  private def initializeGunGroup() {
    gunGroups = new ArrayList()
    for (gunGroupDescriptor <- planeDescriptor.getGunGroupDescriptors) {
      gunGroups.add(new GunGroup(gunGroupDescriptor, projectileManager))
    }
  }

  def update(tpf: Float) {
    if (!crashed) {
      engineSound.play()
      updatePlanePhysics(tpf)
      if (firing) {
        gunSound.play()
      } else {
        gunSound.stop()
      }
      for (gunGroup <- gunGroups) {
        gunGroup.firing(firing, node.getLocalTranslation, physics.getVVelovity, node.getWorldRotation)
      }
    } else {
      engineSound.stop()
      gunSound.stop()
    }
  }

  def setThrottle(throttle: Float) {
    if (throttle < 0.0f || throttle > 1.0f) {
      throw new IllegalArgumentException()
    }
    physics.setThrust(throttle)
    engineSound.setPitch(0.5f + throttle)
  }

  def setAileron(aileron: Float) {
    physics.setAileron(aileron)
  }

  def setElevator(elevator: Float) {
    physics.setElevator(elevator)
  }

  def setRudder(rudder: Float): Unit = {
    physics.setRudder(rudder)
  }

  def setHeight(height: Int) {
    node.getLocalTranslation.setY(height)
  }

  def setLocation(x: Int, z: Int) {
    node.move(x, node.getLocalTranslation.y, z)
  }

  def setLocation(x: Int, y: Int, z: Int) {
    node.move(x, y, z)
  }

  def setLocation(location: Vector3f) {
    node.move(location)
  }

  def getHeight(): Float = node.getLocalTranslation.y

  def getLocation(): Vector3f = node.getLocalTranslation

  def getLocation2D(): Vector2f = {
    new Vector2f(node.getLocalTranslation.x, node.getLocalTranslation.z)
  }

  def getSpeedKmH(): String = physics.getSpeedKmH

  def setFiring(trigger: Boolean) {
    firing = trigger
  }

  def crashed(crashed: Boolean) {
    this.crashed = crashed
  }
}
