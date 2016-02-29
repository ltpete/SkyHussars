package com.codebetyars.skyhussars.engine.plane

import com.codebetyars.skyhussars.SkyHussarsDataModel.PlaneMissionDescriptor
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager
import com.codebetyars.skyhussars.engine.{ModelManager, SoundManager}
import com.jme3.scene.Node
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ModelManager.Models.Spatials._
import ModelManager.Models.Materials._

@Component
class PlaneFactory {

  @Autowired var rootNode: Node = _
  @Autowired var modelManager: ModelManager = _
  @Autowired var soundManager: SoundManager = _
  @Autowired var projectileManager: ProjectileManager = _

  def createPlane(planeMission: PlaneMissionDescriptor): Plane = {
    val model = modelManager.createModel(P80, P80Material).clone()

    // wire the first engine's and gun's sound until further requirement
    val engineSound = soundManager.sound(planeMission.plane.engines.head.engine.sound)
    val gunSound = soundManager.sound(planeMission.plane.guns.head.gunLocations.head.gun.sound)

    val plane = new Plane(model, planeMission, engineSound, gunSound, projectileManager)
    plane.setLocation(planeMission.startLocation)
    plane.setThrottle(0.6f)
    rootNode.attachChild(plane.node)

    plane
  }

}
