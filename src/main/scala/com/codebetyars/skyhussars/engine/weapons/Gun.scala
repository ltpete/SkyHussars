package com.codebetyars.skyhussars.engine.weapons

import java.util.{LinkedList, List}

import com.jme3.math.Vector3f
//remove if not needed

class Gun(private var gunLocation: GunLocationDescriptor) {

  private var ammo: Int = gunLocation.getRoundsMax

  private var bpslu: Float = 0

  def update(tpf: Float, 
      firing: Boolean, 
      noseDirection: Vector3f, 
      vPlaneVelocity: Vector3f, 
      vPlaneLocation: Vector3f): List[Projectile] = {
    bpslu += gunLocation.getGunDescriptor.getRateOfFire * tpf
    val projectiles = new LinkedList[Projectile]()
    bpslu -= 1
    while (bpslu > 1) {
      val bullet = new Bullet(Vector3f.ZERO, Vector3f.ZERO, null)
      projectiles.add(bullet)
      bpslu -= 2
    }
    projectiles
  }
}
