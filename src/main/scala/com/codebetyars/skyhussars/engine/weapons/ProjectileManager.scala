package com.codebetyars.skyhussars.engine.weapons

import java.util.{ArrayList, LinkedList, List}

import com.codebetyars.skyhussars.engine.DataManager
import com.jme3.math.Vector3f
import com.jme3.scene.{Geometry, Node}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
//remove if not needed
import scala.collection.JavaConversions._

@Component
class ProjectileManager {

  @Autowired
  private var rootNode: Node = _

  @Autowired
  private var dataManager: DataManager = _

  private var projectiles: List[Projectile] = new ArrayList()

  private var projectileGeometries: List[Geometry] = new LinkedList()

  def addProjectile(projectile: Bullet) {
    val newGeometry = dataManager.getBullet
    projectileGeometries.add(newGeometry)
    rootNode.attachChild(newGeometry)
    newGeometry.move(projectile.getLocation)
    projectiles.add(projectile)
  }

  def update(tpf: Float) {
    val geomIterator = projectileGeometries.iterator()
    for (projectile <- projectiles) {
      projectile.update(tpf)
      if (geomIterator.hasNext) {
        val geom = geomIterator.next()
        geom.setLocalTranslation(projectile.getLocation)
        val direction = projectile.getVelocity.normalize()
        geom.lookAt(direction, Vector3f.UNIT_Y)
      }
    }
  }
}
