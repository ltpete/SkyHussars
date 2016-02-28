package com.codebetyars.skyhussars.engine.plane

import java.util.Random

import com.codebetyars.skyhussars.SkyHussarsDataModel.GunLocationDescriptor
import com.codebetyars.skyhussars.engine.weapons.{Bullet, ProjectileManager}
import com.jme3.math.{Quaternion, Ring, Vector3f}

class GunLocation(gunLocationDescriptor: GunLocationDescriptor, projectileManager: ProjectileManager) {

  var rounds: Int = gunLocationDescriptor.roundsMax

  val random: Random = new Random()

  def addSpread(vVelocity: Vector3f): Vector3f = {
    val spread = gunLocationDescriptor.gunDescriptor.spread * random.nextGaussian().toFloat * vVelocity.length / 100f
    new Ring(vVelocity, vVelocity.normalize(), spread, spread).random()
  }

  def firing(firing: Boolean, vLocation: Vector3f, vVelocity: Vector3f, vOrientation: Quaternion) {
    if (firing) {
      val vBulletLocation = vLocation.add(vOrientation.mult(gunLocationDescriptor.location))
      val vMuzzleVelocity = vOrientation.mult(Vector3f.UNIT_Z).mult(gunLocationDescriptor.gunDescriptor.muzzleVelocity)
      val vBulletVelocity = addSpread(vVelocity.add(vMuzzleVelocity))
      val bullet = new Bullet(vBulletLocation, vBulletVelocity, null)
      projectileManager.addProjectile(bullet)
    }
  }
}
