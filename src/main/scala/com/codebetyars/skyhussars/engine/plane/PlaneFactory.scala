package com.codebetyars.skyhussars.engine.plane

import com.jme3.scene.Node

import com.codebetyars.skyhussars.SkyHussarsDataModel.PlaneMissionDescriptor
import com.codebetyars.skyhussars.engine.{ModelManager, SoundManager}
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PlaneFactory {

  @Autowired var rootNode: Node = _
  @Autowired var modelManager: ModelManager = _
  @Autowired var soundManager: SoundManager = _
  @Autowired var projectileManager: ProjectileManager = _

  def createPlane(planeMission: PlaneMissionDescriptor): Plane = {
    val model = modelManager.model("p80", "p80_material").clone()

    // we wire the first engine's and gun's sound untile further requirement
    val engineSound = soundManager.sound(planeMission.plane.engines.head.engine.sound)
    val gunSound = soundManager.sound(planeMission.plane.guns.head.gunLocations.head.gun.sound)

    val plane = new Plane(model, planeMission, engineSound, gunSound, projectileManager)
    plane.setLocation(planeMission.startLocation)
    plane.setThrottle(0.6f)
    rootNode.attachChild(plane.node)

    plane
  }

}
