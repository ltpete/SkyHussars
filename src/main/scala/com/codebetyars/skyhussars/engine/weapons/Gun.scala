package com.codebetyars.skyhussars.engine.weapons

import com.codebetyars.skyhussars.SkyHussarsDataModel.GunLocationDescriptor
import com.jme3.math.Vector3f


class Gun(gunLocation: GunLocationDescriptor) {

  var ammo = gunLocation.roundsMax

  var bpslu: Float = 0

  def update(tpf: Float, firing: Boolean, noseDirection: Vector3f, vPlaneVelocity: Vector3f, vPlaneLocation: Vector3f): List[Projectile] = {
    bpslu += gunLocation.gun.rateOfFire * tpf
    val projectiles = List[Projectile]()
    bpslu -= 1
    while (bpslu > 1) {
      val bullet = new Bullet(Vector3f.ZERO, Vector3f.ZERO, null)
      //projectiles.add(bullet)
      bpslu -= 2
    }
    projectiles
  }
}
