package com.codebetyars.skyhussars.engine.physics

import com.codebetyars.skyhussars.utils.Logging
import com.jme3.math.{Quaternion, Vector3f}
import org.junit.Assert.{assertEquals, fail}
import org.junit._

@Ignore
class SymmetricAirfoilTest extends Logging {

  @Test
  def testCalculateResultantForce() {
    logger.debug("calculateResultantForce")
    val airDensity = 0.0F
    val vVelocity: Vector3f = null
    val horizontal: Quaternion = null
    val angularVelocity: Vector3f = null
    val instance: SymmetricAirfoil = null
    val expResult: Vector3f = null
    val result = instance.calculateResultantForce(airDensity, vVelocity, horizontal, angularVelocity)
    assertEquals(expResult, result)
    fail("The test case is a prototype.")
  }

  @Test
  def testCalculateLift_4args() {
    logger.debug("calculateLift")
    val angleOfAttack = 0.0F
    val airDensity = 0.0F
    val vFlow: Vector3f = null
    val situation: Quaternion = null
    val instance: SymmetricAirfoil = null
    val expResult: Vector3f = null
    val result = instance.calculateLift(angleOfAttack, airDensity, vFlow, situation.mult(Vector3f.UNIT_Y))
    assertEquals(expResult, result)
    fail("The test case is a prototype.")
  }

  @Test
  def testCalculateLift_3args() {
    logger.debug("calculateLift")
    val angleOfAttack = 0.0F
    val airDensity = 0.0F
    val vFlow: Vector3f = null
    val instance: SymmetricAirfoil = null
    val expResult = 0.0F
    val result = instance.calculateLift(angleOfAttack, airDensity, vFlow)
    assertEquals(expResult, result, 0.0)
    fail("The test case is a prototype.")
  }

  @Test
  def testGetLiftCoefficient() {
    logger.debug("getLiftCoefficient")
    val angleOfAttack = 0.0F
    val instance: SymmetricAirfoil = null
    val expResult = 0.0F
    val result = instance.getLiftCoefficient(angleOfAttack)
    assertEquals(expResult, result, 0.0)
    fail("The test case is a prototype.")
  }

  @Test
  def testCalculateInducedDrag_3args() {
    logger.debug("calculateInducedDrag")
    val airDensity = 0.0F
    val vFlow: Vector3f = null
    val vLift: Vector3f = null
    val instance: SymmetricAirfoil = null
    val expResult: Vector3f = null
    val result = instance.calculateInducedDrag(airDensity, vFlow, vLift)
    assertEquals(expResult, result)
    fail("The test case is a prototype.")
  }

  @Test
  def testCalculateInducedDrag_float_Vector3f() {
    logger.debug("calculateInducedDrag")
    val airDensity = 0.0F
    val vVelocity: Vector3f = null
    val instance: SymmetricAirfoil = null
    val expResult = 0.0F
    val result = instance.calculateInducedDrag(airDensity, vVelocity)
    assertEquals(expResult, result, 0.0)
    fail("The test case is a prototype.")
  }

  @Test
  def testGetCenterOfGravity() {
    logger.debug("getCenterOfGravity")
    val instance: SymmetricAirfoil = null
    val expResult: Vector3f = null
    val result = instance.getCenterOfGravity
    assertEquals(expResult, result)
    fail("The test case is a prototype.")
  }

  @Test
  def testGetName() {
    logger.debug("getName")
    val instance: SymmetricAirfoil = null
    val expResult = ""
    val result = instance.name
    assertEquals(expResult, result)
    fail("The test case is a prototype.")
  }

  @Test
  def testControlAileron() {
    logger.debug("controlAileron")
    val aileron = 0
    val instance: SymmetricAirfoil = null
    instance.controlAileron(aileron)
    fail("The test case is a prototype.")
  }
}
