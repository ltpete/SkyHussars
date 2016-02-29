package com.codebetyars.skyhussars.engine

import com.codebetyars.skyhussars.engine.ModelManager.{MaterialDescriptor, SpatialDescriptor}

import scala.collection.mutable
import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.scene.Spatial
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

object ModelManager {
  case class SpatialDescriptor(spatialFileLocation: String)
  case class MaterialDescriptor(materialFileLocation: String, shininess: Float, useMaterialColors: Boolean, colorAmbient: ColorRGBA, colorDiffuse: ColorRGBA, colorSpecular: ColorRGBA, textureFileLocation: String)

  object Models {
    object Spatials {
      object P80 extends SpatialDescriptor("Models/p80/p80_16_game.j3o")
    }
    object Materials {
      object P80Material extends MaterialDescriptor(
        "Common/MatDefs/Light/Lighting.j3md",
        shininess = 100f,
        useMaterialColors = true,
        colorAmbient = ColorRGBA.Gray,
        colorDiffuse = ColorRGBA.Gray,
        colorSpecular = ColorRGBA.Gray,
        textureFileLocation = "Textures/p80.png"
      )
    }
  }
}

@Component
class ModelManager extends InitializingBean {

  @Autowired
  private var assetManager: AssetManager = _

  val spatials = mutable.Map[SpatialDescriptor, Spatial]()

  val materials = mutable.Map[MaterialDescriptor, Material]()

  override def afterPropertiesSet() {
    val allSpatials = List[SpatialDescriptor] (
      ModelManager.Models.Spatials.P80
    )
    val allMaterials = List[MaterialDescriptor] (
      ModelManager.Models.Materials.P80Material
    )

    allSpatials.foreach { sd =>
      spatials += sd -> assetManager.loadModel(sd.spatialFileLocation)
    }

    allMaterials.foreach { md =>
      val material = new Material(assetManager, md.materialFileLocation)
      material.setFloat("Shininess", md.shininess)
      material.setBoolean("UseMaterialColors", md.useMaterialColors)
      material.setColor("Ambient", md.colorAmbient)
      material.setColor("Diffuse", md.colorDiffuse)
      material.setColor("Specular", md.colorSpecular)
      material.setTexture("DiffuseMap", assetManager.loadTexture(md.textureFileLocation))
      materials += md -> material
    }
  }

  def createModel(spatial: SpatialDescriptor, material: MaterialDescriptor): Spatial = {
    val model = spatials(spatial).clone()
    model.setMaterial(materials(material))
    model
  }

}
