package com.codebetyars.skyhussars.engine

import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
//remove if not needed

@Component
class DataManager {

  @Autowired
  private var assetManager: AssetManager = _

  private var bulletTemplate: Geometry = _

  def getBullet(): Geometry = {
    if (bulletTemplate == null) {
      val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
      mat.setColor("Color", ColorRGBA.Green)
      val bullet = new Geometry("bullet", new Box(0.2f, 0.2f, 0.2f))
      bullet.setMaterial(mat)
      bulletTemplate = bullet
    }
    bulletTemplate.clone()
  }
}
