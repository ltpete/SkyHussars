package com.codebetyars.skyhussars.engine.weapons

import com.jme3.math.Vector3f
//remove if not needed

class Bullet(vLocation0: Vector3f, vVelocity0: Vector3f, private var bulletDescriptor: BulletDescriptor)
    extends Projectile {

  private var vLocation: Vector3f = vLocation0.clone()

  private var vVelocity: Vector3f = vVelocity0.clone()

  override def update(tpf: Float) {
    vLocation = vLocation.add(vVelocity0.mult(tpf))
  }

  override def getLocation(): Vector3f = vLocation

  override def getVelocity(): Vector3f = vVelocity
}
