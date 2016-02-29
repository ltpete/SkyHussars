package com.codebetyars.skyhussars.engine.physics

import com.codebetyars.skyhussars.utils.Logging
import com.jme3.math.{FastMath, Quaternion, Vector3f}

class SymmetricAirfoil(val name: String, cog: Vector3f, wingArea: Float, incidence: Float, aspectRatio: Float, damper: Boolean, dihedralDegree: Float) extends Airfoil with Logging {

  private var aoa: Array[Int] = Array(0, 2, 4, 6, 8, 10, 12, 30)

  private var clm05: Array[Float] = Array(0f, 0.246f, 0.475f, 0.68f, 0.775f, 0.795f, 0.778f, 0.8f)

  private var qIncidence: Quaternion = new Quaternion().fromAngles((-incidence) * FastMath.DEG_TO_RAD, 0, 0)

  private var dihedral: Quaternion = new Quaternion().fromAngleAxis(dihedralDegree * FastMath.DEG_TO_RAD, Vector3f.UNIT_Z)

  private var qAileron: Quaternion = new Quaternion()

  private var wingRotation: Quaternion = qIncidence.mult(dihedral)

  private var dampingFactor: Float = 1f

  val leftDamper: Boolean = cog.dot(Vector3f.UNIT_X) < 0

  debug(name + " pointing to " + qIncidence.mult(dihedral).mult(Vector3f.UNIT_Y))

  override def calculateResultantForce(airDensity: Float, vFlow0: Vector3f, situation: Quaternion, vAngularVelocity: Vector3f): Vector3f = {
    val foil = situation.mult(wingRotation).mult(qAileron)
    val vUp = foil.mult(Vector3f.UNIT_Y).normalize()
    val vFlow = addDamping(vFlow0, vAngularVelocity, vUp)
    val angleOfAttack = calculateAngleOfAttack(vUp, vFlow.normalize())
    val vLift = calculateLift(angleOfAttack, airDensity, vFlow, vUp)
    val vInducedDrag = calculateInducedDrag(airDensity, vFlow, vLift)
    logging(vLift, vUp, angleOfAttack, vInducedDrag)
    vLift.add(vInducedDrag)
  }

  private def logging(vLift: Vector3f, 
      vUp: Vector3f, 
      angleOfAttack: Float, 
      vInducedDrag: Vector3f) {
    var direction = "up"
    if (vLift.normalize().dot(vUp) < 0) {
      direction = "down"
    }
    logger.debug(name + " at " + angleOfAttack + " degrees generated " + 
      direction + 
      "forces: vLift " + 
      vLift.length + 
      ", induced drag " + 
      vInducedDrag.length)
  }

  def addDamping(vFlow0: Vector3f, vAngularVelocity: Vector3f, vUp: Vector3f): Vector3f = {
    val zDamping = vAngularVelocity.z * cog.length * dampingFactor
    val xDamping = vAngularVelocity.x * cog.length * 2
    val yDamping = vAngularVelocity.y * cog.length * 1
    var vFlow = vFlow0
    if (damper && leftDamper) {
      vFlow = vFlow.add(vUp.mult(zDamping))
    } else if (damper && !leftDamper) {
      vFlow = vFlow.add(vUp.mult(zDamping).negate())
    }
    if (name == "HorizontalStabilizer") {
      vFlow = vFlow.add(vUp.mult(xDamping).negate())
    }
    if (name == "VerticalStabilizer") {
      vFlow = vFlow.add(vUp.mult(yDamping).negate())
    }
    vFlow
  }

  def calculateLift(angleOfAttack: Float, 
      airDensity: Float, 
      vFlow: Vector3f, 
      vUp: Vector3f): Vector3f = {
    val scLift = calculateLift(angleOfAttack, airDensity, vFlow)
    var liftDirection = vFlow.cross(vUp).cross(vFlow).normalize()
    if (angleOfAttack < 0) {
      liftDirection = liftDirection.negate()
    }
    liftDirection.mult(scLift)
  }

  def calculateLift(angleOfAttack: Float, airDensity: Float, vFlow: Vector3f): Float = {
    0.5f * airDensity * getLiftCoefficient(FastMath.abs(angleOfAttack)) * 
      wingArea * 
      vFlow.lengthSquared()
  }

  def getLiftCoefficient(angleOfAttack: Float): Float = {
    var liftCoefficient = 0f
    for (i <- 1 until aoa.length if angleOfAttack < aoa(i)) {
      val diff = aoa(i) - aoa(i - 1)
      val real = angleOfAttack - aoa(i - 1)
      val a = real / diff
      val b = 1f - a
      liftCoefficient = clm05(i) * a + clm05(i - 1) * b
      //break
    }
    liftCoefficient
  }

  def calculateInducedDrag(airDensity: Float, vFlow: Vector3f, vLift: Vector3f): Vector3f = {
    val dividened = 0.5f * airDensity * aspectRatio * vFlow.lengthSquared() * FastMath.PI * wingArea
    if (dividened == 0) {
      return Vector3f.ZERO
    }
    val scInducedDrag = vLift.lengthSquared() / dividened
    vFlow.normalize().mult(scInducedDrag)
  }

  def calculateInducedDrag(airDensity: Float, vVelocity: Vector3f): Float = {
    throw new UnsupportedOperationException("Not supported yet.")
  }

  def getCenterOfGravity: Vector3f = cog

  private def calculateAngleOfAttack(vUp: Vector3f, vFlow: Vector3f): Float = {
    var angleOfAttack = vFlow.cross(vUp).cross(vFlow).normalize().angleBetween(vUp) * 
      FastMath.RAD_TO_DEG
    val np = vUp.dot(vFlow)
    if (np < 0) {
      angleOfAttack = -angleOfAttack
    }
    angleOfAttack
  }

  def controlAileron(aileron: Float) {
    val q = new Quaternion()
    qAileron = q.fromAngles(aileron * FastMath.DEG_TO_RAD, 0, 0)
  }
}
