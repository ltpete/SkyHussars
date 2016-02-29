package com.codebetyars.skyhussars

import com.codebetyars.skyhussars.utils.Logging
import com.jme3.app.SimpleApplication
import com.jme3.renderer.RenderManager
import com.jme3.system.AppSettings
import org.springframework.context.annotation.AnnotationConfigApplicationContext
//remove if not needed

object SkyHussars extends Logging {

  def main(args: Array[String]) {
    val settings = new AppSettings(false)
    settings.setTitle("SkyHussars")
    settings.setSettingsDialogImage("Textures/settings_image.jpg")
    val application = new SkyHussars()
    application.setSettings(settings)
    info("Starting SkyHussars...")
    application.start()
  }

}

class SkyHussars extends SimpleApplication {

  private var skyHussarsContext: SkyHussarsContext = _

  override def simpleInitApp() {
    val context = new AnnotationConfigApplicationContext()
    val beanFactory = context.getDefaultListableBeanFactory

    Map(
      "application" ->  this,
      "rootNode" -> getRootNode,
      "assetManager" -> getAssetManager,
      "inputManager" -> getInputManager,
      "camera" -> getCamera,
      "flyByCamera" -> getFlyByCamera,
      "audioRenderer" -> getAudioRenderer,
      "guiViewPort" -> getGuiViewPort
    ).foreach{ case (name, singleton) =>
      beanFactory.registerSingleton(name, singleton)
    }

    context.register(classOf[SkyHussarsContext])
    context.refresh()

    skyHussarsContext = context.getBean(classOf[SkyHussarsContext])
    skyHussarsContext.simpleInitApp()
    setDisplayStatView(false)
  }

  override def simpleUpdate(tpf: Float) {
    skyHussarsContext.simpleUpdate(tpf)
  }

  override def simpleRender(rm: RenderManager) {
    skyHussarsContext.simpleRender(rm)
  }
}
