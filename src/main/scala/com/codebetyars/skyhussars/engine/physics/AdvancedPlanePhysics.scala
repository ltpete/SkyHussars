package com.codebetyars.skyhussars.engine.physics

import java.text.NumberFormat

import com.codebetyars.skyhussars.SkyHussarsDataModel._
import com.codebetyars.skyhussars.engine.physics.AdvancedPlanePhysics._
import com.jme3.math.{FastMath, Matrix3f, Quaternion, Vector3f}
import com.jme3.scene.Spatial
import org.slf4j.LoggerFactory

object AdvancedPlanePhysics {

  private val logger = LoggerFactory.getLogger(classOf[AdvancedPlanePhysics])

  private val GRAVITY = new Vector3f(0f, -10f, 0f)
}

class AdvancedPlanePhysics(planeDescriptor: PlaneDescriptor)
    extends PlanePhysics {

  private var airDensity: Float = 1.2745f

  private var planeFactor: Float = 0.2566f

  private var wingArea: Float = 22.07f

  private var mass: Float = planeDescriptor.massGross

  private var vWeight: Vector3f = GRAVITY.mult(mass)

  private var aspectRatio: Float = 6.37f

  private var pi: Float = 3.14f

  private var angleOfAttack: Float = _

  private var vVelocity: Vector3f = new Vector3f(0f, 0f, 0f)

  private var vAcceleration: Vector3f = new Vector3f(0f, 0f, 0f)

  private var vDrag: Vector3f = new Vector3f(0f, 0f, 0f)

  private var vLift: Vector3f = new Vector3f(0f, 0f, 0f)

  private var vAngularAcceleration: Vector3f = new Vector3f(0, 0, 0)

  private var vAngularVelocity: Vector3f = new Vector3f(0, 0, 0)

  private var height: Float = _

  private var length: Float = 10.49f

  private var rPlane: Float = 1.3f

  private var momentOfInertiaTensor: Matrix3f = new Matrix3f((mass / 12) * (3 * rPlane * rPlane + length * length), 
    0f, 0f, 0f, (mass / 12) * (3 * rPlane * rPlane + length * length), 0f, 0f, 0f, (mass / 2) * (rPlane * rPlane))

  private var airfoils: List[Airfoil] = new ArrayList()

  private var engines: List[Engine] = new ArrayList()

  var leftWing: SymmetricAirfoil = new SymmetricAirfoil("WingA", new Vector3f(-2.0f, 0, -0.2f), wingArea / 2, 
    1f, aspectRatio, true, 0f)

  var rightWing: SymmetricAirfoil = new SymmetricAirfoil("WingB", new Vector3f(2.0f, 0, -0.2f), wingArea / 2, 
    1f, aspectRatio, true, 0f)

  var horizontalStabilizer: SymmetricAirfoil = new SymmetricAirfoil("HorizontalStabilizer", new Vector3f(0, 
    0, -6.0f), 5f, -3f, aspectRatio / 1.5f, false, 0f)

  var verticalStabilizer: SymmetricAirfoil = new SymmetricAirfoil("VerticalStabilizer", new Vector3f(0, 
    0, -6.0f), 5.0f, 0f, aspectRatio / 1.5f, false, 90f)

  airfoils.add(leftWing)

  airfoils.add(rightWing)

  airfoils.add(horizontalStabilizer)

  airfoils.add(verticalStabilizer)

  for (engineLocation <- planeDescriptor.getEngineLocations) {
    engines.add(new Engine(engineLocation))
  }

  override def update(tpf: Float, model: Spatial) {
    updateAuxiliary(model)
    var vLinearAcceleration = Vector3f.ZERO
    logger.debug("Plane roll: " + 
      (model.getLocalRotation.mult(Vector3f.UNIT_X).cross(Vector3f.UNIT_Z.negate())
      .angleBetween(Vector3f.UNIT_Y) * 
      FastMath.RAD_TO_DEG))
    val engineForces = calculateEngineForces(model.getLocalRotation)
    val airfoilForces = calculateAirfoilForces(model.getLocalRotation, vVelocity.negate())
    logger.debug("Airfoil linear: " + airfoilForces.vLinearComponent.length + 
      ", torque: " + 
      airfoilForces.vTorqueComponent.length)
    vLinearAcceleration = vLinearAcceleration.add(vWeight)
    vLinearAcceleration = vLinearAcceleration.add(engineForces.vLinearComponent)
    vLinearAcceleration = vLinearAcceleration.add(airfoilForces.vLinearComponent)
    vLinearAcceleration = vLinearAcceleration.add(calculateParasiticDrag())
    vLinearAcceleration = vLinearAcceleration.divide(mass)
    vVelocity = vVelocity.add(vLinearAcceleration.mult(tpf))
    model.move(vVelocity.mult(tpf))
    vAngularAcceleration = momentOfInertiaTensor.invert().mult(airfoilForces.vTorqueComponent)
    vAngularVelocity = vAngularVelocity.add(vAngularAcceleration.mult(tpf))
    logger.debug("Angular velocity: " + vAngularVelocity)
    moderateRoll()
    model.rotate(vAngularVelocity.x * tpf, vAngularVelocity.y * tpf, vAngularVelocity.z * tpf)
  }

  private def moderateRoll() {
    if (vAngularVelocity.x > 2) {
      vAngularVelocity.x = 2
    } else if (vAngularVelocity.x < -2) {
      vAngularVelocity.x = -2
    }
    if (vAngularVelocity.y > 2) {
      vAngularVelocity.y = 2
    } else if (vAngularVelocity.y < -2) {
      vAngularVelocity.y = -2
    }
    if (vAngularVelocity.z > 2) {
      vAngularVelocity.z = 2
    } else if (vAngularVelocity.z < -2) {
      vAngularVelocity.z = -2
    }
  }

  private def calculateEngineForces(situation: Quaternion): ActingForces = {
    var vLinearAcceleration = Vector3f.ZERO
    for (engine <- engines) {
      vLinearAcceleration = vLinearAcceleration.add(situation.mult(engine.getThrust))
    }
    new ActingForces(vLinearAcceleration, Vector3f.ZERO)
  }

  private def calculateAirfoilForces(situation: Quaternion, vFlow: Vector3f): ActingForces = {
    var vLinearAcceleration = Vector3f.ZERO
    var vTorque = Vector3f.ZERO
    for (airfoil <- airfoils) {
      var airfoilForce = airfoil.calculateResultantForce(airDensity, vFlow, situation, vAngularVelocity)
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
    vVelocity.negate().normalize().mult(airDensity * planeFactor * vVelocity.lengthSquared())
  }

  private def updateAuxiliary(model: Spatial) {
    updateHelpers(model)
    updateAngleOfAttack(model)
    updatePlaneFactor()
  }

  private def updateAngleOfAttack(model: Spatial) {
    angleOfAttack = model.getLocalRotation.mult(Vector3f.UNIT_Z).angleBetween(vVelocity.normalize()) * 
      FastMath.RAD_TO_DEG
    val np = model.getLocalRotation.mult(Vector3f.UNIT_Y).dot(vVelocity.negate().normalize())
    if (np < 0) {
      angleOfAttack = -angleOfAttack
    }
  }

  def updatePlaneFactor() {
    planeFactor = 0.2566f
  }

  private def updateHelpers(model: Spatial) {
    height = model.getWorldTranslation.getY
    airDensity = WorldPhysicsData.getAirDensity(height.toInt)
  }

  override def setThrust(throttle: Float) {
    for (engine <- engines) {
      engine.setThrottle(throttle)
    }
  }

  override def getSpeedKmH(): String = {
    val fractionless = NumberFormat.getInstance
    fractionless.setMaximumFractionDigits(0)
    fractionless.setMinimumIntegerDigits(3)
    val bigFractionless = NumberFormat.getInstance
    bigFractionless.setMaximumFractionDigits(0)
    bigFractionless.setMinimumIntegerDigits(6)
    fractionless.format(vVelocity.length * 3.6)
  }

  override def getInfo(): String = {
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
    "Thrust: " + engines.get(0).getThrust.length + ", Acceleration: " + 
      accF.format(vAcceleration.length) + 
      ", CurrentSpeed: " + 
      fractionless.format(vVelocity.length) + 
      ", CurrentSpeed km/h: " + 
      fractionless.format(vVelocity.length * 3.6) + 
      ", Drag: " + 
      bigFractionless.format(vDrag.length) + 
      ", Height: " + 
      fractionless.format(height) + 
      ", Lift: " + 
      bigFractionless.format(vLift.length) + 
      ", AOA: " + 
      fractionless.format(angleOfAttack) + 
      ", AngularVelocity: " + 
      fraction2Format.format(vAngularVelocity.length) + 
      ", AngularAcceleration: " + 
      fraction2Format.format(vAngularAcceleration.length)
  }

  override def setElevator(aileron: Float) {
    horizontalStabilizer.controlAileron(5f * aileron)
  }

  override def setSpeedForward(model: Spatial, kmh: Float) {
    vVelocity = model.getLocalRotation.mult(Vector3f.UNIT_Z).normalize()
      .mult(kmh / 3.6f)
  }

  override def setAileron(aileron: Float) {
    leftWing.controlAileron(aileron)
    rightWing.controlAileron(-1f * aileron)
  }

  def setRudder(aileron: Float) {
    verticalStabilizer.controlAileron(aileron)
  }

  override def getVVelovity(): Vector3f = vVelocity
}
