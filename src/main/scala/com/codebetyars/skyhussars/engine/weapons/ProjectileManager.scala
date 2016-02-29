package com.codebetyars.skyhussars.engine.weapons

import com.codebetyars.skyhussars.engine.DataManager
import com.jme3.math.Vector3f
import com.jme3.scene.{Geometry, Node}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.mutable

@Component
class ProjectileManager {

  @Autowired
  var rootNode: Node = _

  @Autowired
  var dataManager: DataManager = _

  var projectiles = mutable.Map[Projectile, Geometry]()


  def addProjectile(bullet: Bullet) {
    val geometry = dataManager.getBullet
    rootNode.attachChild(geometry)
    geometry.move(bullet.getLocation)

    projectiles += bullet -> geometry
  }

  def update(tpf: Float) {
    projectiles.foreach { case (projectile, geometry) =>
      projectile.update(tpf)
      geometry.setLocalTranslation(projectile.getLocation)
      geometry.lookAt(projectile.getVelocity.normalize(), Vector3f.UNIT_Y)
    }
  }

}
