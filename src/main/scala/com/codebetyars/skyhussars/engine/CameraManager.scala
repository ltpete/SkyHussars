package com.codebetyars.skyhussars.engine

import com.jme3.input.FlyByCamera
import com.jme3.math.Vector3f
import com.jme3.renderer.Camera
import com.jme3.scene.Spatial
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CameraManager {

  @Autowired
  var camera: Camera = _

  @Autowired
  var flyByCamera: FlyByCamera = _

  var fovChangeActive: Boolean = false
  var fovChangeRate: Float = 8f
  var fovNarrowing: Boolean = false
  val minFov = 20
  val maxFov = 100
  var fov: Float = 0f

  var focus: Spatial = null

  def update(tpf: Float) {
    follow()
    updateFov(tpf)
  }

  def updateFov(tpf: Float) {
    if (fovChangeActive) {
      if (fovNarrowing && fov > minFov) {
        setFov(fov - fovChangeRate * tpf)
      }
      if (!fovNarrowing && fov < maxFov) {
        setFov(fov + fovChangeRate * tpf)
      }
    }
  }

  def follow() {
    val cameraLocation = new Vector3f(0, 3.5f, -12)
    camera.setLocation(focus.getWorldTranslation.add(focus.getLocalRotation.mult(cameraLocation)))
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

  val near: Float = 0.1f
  val far: Float = 200000f
  var aspect: Float = 1f

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
