package com.codebetyars.skyhussars.engine.weapons

import com.codebetyars.skyhussars.SkyHussarsDataModel.BulletDescriptor
import com.jme3.math.Vector3f

class Bullet(vLocation0: Vector3f, vVelocity0: Vector3f, bulletDescriptor: BulletDescriptor) extends Projectile {

  var vLocation: Vector3f = vLocation0.clone()
  var vVelocity: Vector3f = vVelocity0.clone()

  def getLocation: Vector3f = vLocation

  def getVelocity: Vector3f = vVelocity

  def update(tpf: Float) {
    vLocation = vLocation.add(vVelocity0.mult(tpf))
  }

}
