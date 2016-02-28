package com.codebetyars.skyhussars.engine.weapons

import com.jme3.math.Vector3f

trait Projectile {

  def getVelocity: Vector3f

  def getLocation: Vector3f

  def update(tpf: Float): Unit

}
