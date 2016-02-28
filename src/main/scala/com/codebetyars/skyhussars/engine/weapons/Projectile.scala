package com.codebetyars.skyhussars.engine.weapons

import com.jme3.math.Vector3f
//remove if not needed

abstract class Projectile {

  def update(tpf: Float): Unit

  def getVelocity(): Vector3f

  def getLocation(): Vector3f
}
