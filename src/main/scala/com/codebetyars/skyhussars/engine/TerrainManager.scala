package com.codebetyars.skyhussars.engine

import com.codebetyars.skyhussars.engine.plane.Plane
import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.math.Vector2f
import com.jme3.renderer.Camera
import com.jme3.scene.Node
import com.jme3.terrain.geomipmap.{TerrainLodControl, TerrainQuad}
import com.jme3.terrain.heightmap.ImageBasedHeightMap
import com.jme3.texture.Texture
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.beans.BeanProperty


@Component
class TerrainManager extends InitializingBean {

  @Autowired
  private var assetManager: AssetManager = _

  @Autowired
  private var camera: Camera = _

  @Autowired
  private var rootNode: Node = _

  @BeanProperty
  var terrain: TerrainQuad = _

  override def afterPropertiesSet() {
    val heightmap = new ImageBasedHeightMap(assetManager.loadTexture("Textures/Adria.bmp").getImage, 
      1f)
    heightmap.load()
    val grass = assetManager.loadTexture("Textures/ground.png")
    grass.setWrap(Texture.WrapMode.Repeat)
    val water = assetManager.loadTexture("Textures/water.png")
    water.setWrap(Texture.WrapMode.Repeat)
    val land = assetManager.loadTexture("Textures/forest.png")
    land.setWrap(Texture.WrapMode.Repeat)
    val mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md")
    mat_terrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/Adria_alpha.png"))
    mat_terrain.setTexture("DiffuseMap", grass)
    mat_terrain.setFloat("DiffuseMap_0_scale", 4096f)
    mat_terrain.setTexture("DiffuseMap_2", water)
    mat_terrain.setFloat("DiffuseMap_2_scale", 32f)
    mat_terrain.setTexture("DiffuseMap_1", land)
    mat_terrain.setFloat("DiffuseMap_1_scale", 128f)
    terrain = new TerrainQuad("my terrain", 17, 2049, heightmap.getHeightMap)
    terrain.setMaterial(mat_terrain)
    terrain.setLocalScale(1000f, 1f, 1000f)
    terrain.addControl(new TerrainLodControl(terrain, camera))
    rootNode.attachChild(terrain)
  }

  def getHeightAt(at: Vector2f): Float = terrain.getHeight(at)

  def checkCollisionWithGround(plane: Plane): Boolean = {
    var collide = false
    val height = terrain.getHeight(plane.getLocation2D)
    if (height > plane.getHeight) {
      collide = true
    }
    collide
  }
}
