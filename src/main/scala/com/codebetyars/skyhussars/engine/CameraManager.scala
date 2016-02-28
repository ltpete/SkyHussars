package com.codebetyars.skyhussars.engine

import com.jme3.input.FlyByCamera
import com.jme3.math.Vector3f
import com.jme3.renderer.Camera
import com.jme3.scene.Spatial
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
//remove if not needed

@Component
class CameraManager {

  @Autowired
  private var camera: Camera = _

  @Autowired
  private var flyByCamera: FlyByCamera = _

  private var fovChangeActive: Boolean = _

  private var fovNarrowing: Boolean = _

  private val minFov = 20

  private val maxFov = 100

  private var fovChangeRate: Float = 8f

  var focus: Spatial = _

  def update(tpf: Float) {
    follow()
    updateFov(tpf)
  }

  private def updateFov(tpf: Float) {
    if (fovChangeActive) {
      if (fovNarrowing && fov > minFov) {
        setFov(fov - fovChangeRate * tpf)
      }
      if (!fovNarrowing && fov < maxFov) {
        setFov(fov + fovChangeRate * tpf)
      }
    }
  }

  private def follow() {
    val cameraLocation = new Vector3f(0, 3.5f, -12)
    camera.setLocation((focus.getWorldTranslation).add(focus.getLocalRotation.mult(cameraLocation)))
    camera.lookAt(focus.getWorldTranslation, focus.getLocalRotation.mult(Vector3f.UNIT_Y))
  }

  def init() {
    if (focus != null) {
      follow()
    }
  }

  def followWithCamera(spatial: Spatial) {
    this.focus = spatial
  }

  private var aspect: Float = _

  var fov: Float = 0f

  private var near: Float = 0.1f

  private var far: Float = 200000f

  def initializeCamera() {
    aspect = camera.getWidth.toFloat / camera.getHeight.toFloat
    setFov(45)
    flyByCamera.setMoveSpeed(200)
  }

  def setFov(fov: Float) {
    this.fov = fov
    camera.setFrustumPerspective(fov, aspect, near, far)
  }

  def setFovChangeActive(fovChangeActive: Boolean) {
    this.fovChangeActive = fovChangeActive
  }

  def setFovChangeActive(active: Boolean, fovNarrowing: Boolean) {
    this.fovChangeActive = active
    this.fovNarrowing = fovNarrowing
  }

  def moveCameraTo(location: Vector3f) {
    camera.setLocation(location)
  }

  def flyCamActive(cursor: Boolean) {
    flyByCamera.setEnabled(!cursor)
  }

  def flyCamActive(): Boolean = flyByCamera.isEnabled
}
