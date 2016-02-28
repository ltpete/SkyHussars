package com.codebetyars.skyhussars.engine.plane

import com.codebetyars.skyhussars.SkyHussarsDataModel.GunGroupDescriptor
import com.codebetyars.skyhussars.engine.weapons.ProjectileManager
import com.jme3.math.{Quaternion, Vector3f}

class GunGroup(gunGroupDescriptor: GunGroupDescriptor, projectileManager: ProjectileManager) {

  val name: String = gunGroupDescriptor.name

  val gunLocations: List[GunLocation] = gunGroupDescriptor.gunLocations.map(new GunLocation(_, projectileManager))

  def fire(vLocation: Vector3f, vVelocity: Vector3f, vOrientation: Quaternion) {
    gunLocations.foreach(_.fire(vLocation, vVelocity, vOrientation))
  }

}
