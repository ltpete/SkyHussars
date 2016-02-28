package com.codebetyars.skyhussars.engine

import java.util.{HashMap, Map}

import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.scene.Spatial
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
//remove if not needed

@Component
class ModelManager extends InitializingBean {

  @Autowired
  private var assetManager: AssetManager = _

  private var spatials: Map[String, Spatial] = new HashMap()

  private var materials: Map[String, Material] = new HashMap()

  override def afterPropertiesSet() {
    loadModels()
    loadMaterials()
  }

  private def loadModels() {
    val p80 = assetManager.loadModel("Models/p80/p80_16_game.j3o")
    spatials.put("p80", p80)
  }

  private def loadMaterials() {
    val material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md")
    val texture = assetManager.loadTexture("Textures/p80.png")
    material.setFloat("Shininess", 100f)
    material.setBoolean("UseMaterialColors", true)
    material.setColor("Ambient", ColorRGBA.Gray)
    material.setColor("Diffuse", ColorRGBA.Gray)
    material.setColor("Specular", ColorRGBA.Gray)
    material.setTexture("DiffuseMap", texture)
    materials.put("p80_material", material)
  }

  def model(modelName: String, materialName: String): Spatial = {
    val model = spatials.get(modelName).clone()
    model.setMaterial(materials.get(materialName))
    model
  }
}
