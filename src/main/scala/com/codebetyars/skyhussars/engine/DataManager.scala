package com.codebetyars.skyhussars.engine

import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DataManager extends InitializingBean {

  @Autowired
  var assetManager: AssetManager = _

  var bulletTemplate: Geometry = _

  def afterPropertiesSet() {
    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", ColorRGBA.Green)

    bulletTemplate = new Geometry("bullet", new Box(0.2f, 0.2f, 0.2f))
    bulletTemplate.setMaterial(mat)
  }

  def getBullet = bulletTemplate.clone()

}
