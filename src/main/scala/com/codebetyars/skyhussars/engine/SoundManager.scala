package com.codebetyars.skyhussars.engine

import com.codebetyars.skyhussars.engine.SoundManager.SoundDescriptor
import com.jme3.asset.AssetManager
import com.jme3.audio.AudioNode
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object SoundManager {
  case class SoundDescriptor(fileLocation: String, looping: Boolean = false, positional: Boolean = false, volume: Float = 1, pitch: Float = 1)

  object Sounds {
    object Guns {
      object Browning extends SoundDescriptor("Sounds/shoot.ogg", true, true)
    }
    object Engines {
      object Allison extends SoundDescriptor("Sounds/jet.wav", true, true, 3, 1)
    }
  }
}

@Component
class SoundManager extends InitializingBean {

  @Autowired var assetManager: AssetManager = _

  val preparedNodes = mutable.Map[SoundDescriptor, AudioNode]()

  val requestedNodes = ListBuffer[AudioNode]()

  def afterPropertiesSet() {
    val allSounds = List[SoundDescriptor](
      SoundManager.Sounds.Guns.Browning,
      SoundManager.Sounds.Engines.Allison
    )

    allSounds.foreach { sd =>
      val node = new AudioNode(assetManager, sd.fileLocation, false)
      node.setLooping(sd.looping)
      node.setPositional(sd.positional)
      node.setVolume(sd.volume)
      node.setPitch(sd.pitch)
      preparedNodes += sd -> node
    }
  }

  def sound(soundDescriptor: SoundDescriptor): AudioNode = {
    val node = preparedNodes(soundDescriptor).clone()
    requestedNodes += node
    node
  }

  def muteAllSounds() {
    requestedNodes.foreach(_.stop())
  }

}
