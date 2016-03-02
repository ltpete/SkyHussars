package com.codebetyars.skyhussars.engine.physics

import java.text.NumberFormat

import com.codebetyars.skyhussars.SkyHussarsDataModel._
import com.codebetyars.skyhussars.engine.plane.Plane
import com.codebetyars.skyhussars.utils.Logging
import WorldPhysicsData._

import com.jme3.math._
import com.jme3.scene.Spatial

class AdvancedPlanePhysics(plane: Plane) extends Logging {

  def mass = plane.mass // mass can change during flight
  val wingArea = plane.planeDescriptor.wingArea
  val wingRatio = plane.planeDescriptor.wingRatio
  val length = plane.planeDescriptor.length
  val rPlane: Float = 1.3f // update name

  var altitude = 0.0f
  var angleOfAttack = 0.0f
  var vVelocity = new Vector3f
  var vAcceleration = new Vector3f
  var vDrag = new Vector3f
  var vLift = new Vector3f
  var vAngularAcceleration = new Vector3f
  var vAngularVelocity = new Vector3f

  val momentOfInertiaTensor = new Matrix3f(
    (mass / 12) * (3 * rPlane * rPlane + length * length), 0f, 0f,
    0f, (mass / 12) * (3 * rPlane * rPlane + length * length), 0f,
    0f, 0f, (mass / 2) * (rPlane * rPlane)
  )

  // these should be plane specific
  val leftWing: SymmetricAirfoil = new SymmetricAirfoil("WingA", new Vector3f(-2.0f, 0, -0.2f), wingArea / 2, 1f, wingRatio, true, 0f)
  val rightWing: SymmetricAirfoil = new SymmetricAirfoil("WingB", new Vector3f(2.0f, 0, -0.2f), wingArea / 2, 1f, wingRatio, true, 0f)
  val horizontalStabilizer: SymmetricAirfoil = new SymmetricAirfoil("HorizontalStabilizer", new Vector3f(0, 0, -6.0f), 5f, -3f, wingRatio / 1.5f, false, 0f)
  val verticalStabilizer: SymmetricAirfoil = new SymmetricAirfoil("VerticalStabilizer", new Vector3f(0, 0, -6.0f), 5.0f, 0f, wingRatio / 1.5f, false, 90f)

  val airfoils = List(leftWing, rightWing, horizontalStabilizer, verticalStabilizer)
  val engines = plane.planeDescriptor.engines.map(new Engine(_))

   def update(model: Spatial, tpf: Float) {
    updateAuxiliary(model)
    var vLinearAcceleration = Vector3f.ZERO
    logger.debug("Plane roll: " + (model.getLocalRotation.mult(Vector3f.UNIT_X).cross(Vector3f.UNIT_Z.negate()).angleBetween(Vector3f.UNIT_Y) * FastMath.RAD_TO_DEG))
    val engineForces = calculateEngineForces(model.getLocalRotation)
    val airfoilForces = calculateAirfoilForces(model.getLocalRotation, vVelocity.negate())
    logger.debug("Airfoil linear: " + airfoilForces.vLinearComponent.length + ", torque: " + airfoilForces.vTorqueComponent.length)
    vLinearAcceleration = vLinearAcceleration.add(Gravity.mult(mass))
    vLinearAcceleration = vLinearAcceleration.add(engineForces.vLinearComponent)
    vLinearAcceleration = vLinearAcceleration.add(airfoilForces.vLinearComponent)
    vLinearAcceleration = vLinearAcceleration.add(calculateParasiticDrag())
    vLinearAcceleration = vLinearAcceleration.divide(mass)
    vVelocity = vVelocity.add(vLinearAcceleration.mult(tpf))
    model.move(vVelocity.mult(tpf))
    vAngularAcceleration = momentOfInertiaTensor.invert().mult(airfoilForces.vTorqueComponent)
    vAngularVelocity = vAngularVelocity.add(vAngularAcceleration.mult(tpf))
    logger.debug("Angular velocity: " + vAngularVelocity)

    vAngularVelocity = vAngularVelocity.set(
      FastMath.clamp(vAngularVelocity.x, -2, 2),
      FastMath.clamp(vAngularVelocity.y, -2, 2),
      FastMath.clamp(vAngularVelocity.z, -2, 2)
    )
    logger.debug("Moderated angular velocity: " + vAngularVelocity)


    model.rotate(vAngularVelocity.x * tpf, vAngularVelocity.y * tpf, vAngularVelocity.z * tpf)
  }

  private def calculateEngineForces(situation: Quaternion): ActingForces = {
    new ActingForces(engines.map(_.getThrust).map(situation.mult).foldLeft(Vector3f.ZERO) { (a, b) => a.add(b) } , Vector3f.ZERO)
  }

  private def calculateAirfoilForces(situation: Quaternion, vFlow: Vector3f): ActingForces = {
    var vLinearAcceleration = Vector3f.ZERO
    var vTorque = Vector3f.ZERO
    for (airfoil <- airfoils) {
      var airfoilForce = airfoil.calculateResultantForce(AirDensity(altitude), vFlow, situation, vAngularVelocity)
      logger.debug("Airfoilforce points to: " + airfoilForce.toString)
      vLinearAcceleration = vLinearAcceleration.add(airfoilForce)
      airfoilForce = situation.inverse().mult(airfoilForce)
      val distFromCenter = airfoil.getCenterOfGravity
      logger.debug("Airfoilforce points to: " + airfoilForce.toString)
      vTorque = vTorque.add(distFromCenter.cross(airfoilForce))
    }
    new ActingForces(vLinearAcceleration, vTorque)
  }

  private def calculateParasiticDrag(): Vector3f = {
    vVelocity.negate().normalize().mult(AirDensity(altitude) * plane.planeDescriptor.dragFactor * vVelocity.lengthSquared())
  }

  private def updateAuxiliary(model: Spatial) {
    updateHelpers(model)
    updateAngleOfAttack(model)
  }

  private def updateAngleOfAttack(model: Spatial) {
    angleOfAttack = model.getLocalRotation.mult(Vector3f.UNIT_Z).angleBetween(vVelocity.normalize()) *
      FastMath.RAD_TO_DEG
    val np = model.getLocalRotation.mult(Vector3f.UNIT_Y).dot(vVelocity.negate().normalize())
    if (np < 0) {
      angleOfAttack = -angleOfAttack
    }
  }

  private def updateHelpers(model: Spatial) {
    altitude = model.getWorldTranslation.getY
  }

  def setThrust(throttle: Float) {
    for (engine <- engines) {
      engine.setThrottle(throttle)
    }
  }

  def getSpeedKmH = {
    val fractionless = NumberFormat.getInstance
    fractionless.setMaximumFractionDigits(0)
    fractionless.setMinimumIntegerDigits(3)
    val bigFractionless = NumberFormat.getInstance
    bigFractionless.setMaximumFractionDigits(0)
    bigFractionless.setMinimumIntegerDigits(6)
    fractionless.format(vVelocity.length * 3.6)
  }

  def getInfo = {
    val fraction2Format = NumberFormat.getInstance
    fraction2Format.setMaximumFractionDigits(2)
    fraction2Format.setMinimumFractionDigits(2)
    val accF = NumberFormat.getInstance
    accF.setMaximumFractionDigits(2)
    accF.setMinimumFractionDigits(2)
    accF.setMaximumIntegerDigits(3)
    accF.setMinimumIntegerDigits(3)
    val fractionless = NumberFormat.getInstance
    fractionless.setMaximumFractionDigits(0)
    fractionless.setMinimumIntegerDigits(3)
    val bigFractionless = NumberFormat.getInstance
    bigFractionless.setMaximumFractionDigits(0)
    bigFractionless.setMinimumIntegerDigits(6)

    "Thrust: " + engines.head.getThrust.length +
    ", Acceleration: " + accF.format(vAcceleration.length) +
    ", CurrentSpeed: " + fractionless.format(vVelocity.length) +
    ", CurrentSpeed km/h: " + fractionless.format(vVelocity.length * 3.6) +
    ", Drag: " + bigFractionless.format(vDrag.length) +
    ", Height: " + fractionless.format(altitude) +
    ", Lift: " + bigFractionless.format(vLift.length) +
    ", AOA: " + fractionless.format(angleOfAttack) +
    ", AngularVelocity: " + fraction2Format.format(vAngularVelocity.length) +
    ", AngularAcceleration: " + fraction2Format.format(vAngularAcceleration.length)
  }

  def setSpeedForward(model: Spatial, kmh: Float) {
    vVelocity = model.getLocalRotation.mult(Vector3f.UNIT_Z).normalize().mult(kmh / 3.6f)
  }

  def setElevator(aileron: Float) {
    horizontalStabilizer.controlAileron(5f * aileron)
  }

  def setAileron(aileron: Float) {
    leftWing.controlAileron(+aileron)
    rightWing.controlAileron(-aileron)
  }

  def setRudder(aileron: Float) {
    verticalStabilizer.controlAileron(aileron)
  }

  def getVVelovity = vVelocity
}

case class ActingForces(vLinearComponent: Vector3f, vTorqueComponent: Vector3f)