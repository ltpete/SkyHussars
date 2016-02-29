package com.codebetyars.skyhussars.engine.physics

import com.jme3.math.{FastMath, Quaternion, Vector3f}
import org.junit.Test

class GeneralTest {

  private var aspectRatio: Float = 6.37f

  private var wingArea: Float = 11f

  private var airDensity: Float = 1.2745f

  @Test
  def general0AoATest() {
    val wingLocation = new Vector3f(0, 0, 0)
    val incidence = 0
    val dihedralDegree = 0
    val test = new SymmetricAirfoil("WingA", wingLocation, wingArea, incidence, aspectRatio, true, dihedralDegree)
    val flow = Vector3f.UNIT_Z.mult(300).negate()
    val situation = new Quaternion()
    val angularVelocity = Vector3f.ZERO
    test.calculateResultantForce(airDensity, flow, situation, angularVelocity)
  }

  @Test
  def generalIncidenceTestAt0AoA() {
    val wingLocation = new Vector3f(0, 0, 0)
    val incidence = 1
    val dihedralDegree = 0
    val test = new SymmetricAirfoil("WingA", wingLocation, wingArea, incidence, aspectRatio, true, dihedralDegree)
    val flow = Vector3f.UNIT_Z.mult(300).negate()
    val situation = new Quaternion()
    val angularVelocity = Vector3f.ZERO
    test.calculateResultantForce(airDensity, flow, situation, angularVelocity)
  }

  @Test
  def general90DegreeRotationTestAt0AoA() {
    val wingLocation = new Vector3f(0, 0, 0)
    val incidence = 1
    val dihedralDegree = 0
    val test = new SymmetricAirfoil("WingA", wingLocation, wingArea, incidence, aspectRatio, true, dihedralDegree)
    val flow = Vector3f.UNIT_Z.mult(300).negate()
    val situation = new Quaternion().fromAngles(0, 0, 90 * FastMath.DEG_TO_RAD)
    val angularVelocity = Vector3f.ZERO
    test.calculateResultantForce(airDensity, flow, situation, angularVelocity)
  }

  @Test
  def general180DegreeRotationTestAt0AoA() {
    val wingLocation = new Vector3f(0, 0, 0)
    val incidence = 1
    val dihedralDegree = 0
    val test = new SymmetricAirfoil("WingA", wingLocation, wingArea, incidence, aspectRatio, true, dihedralDegree)
    val flow = Vector3f.UNIT_Z.mult(300).negate()
    val situation = new Quaternion().fromAngles(0, 0, 180 * FastMath.DEG_TO_RAD)
    val angularVelocity = Vector3f.ZERO
    test.calculateResultantForce(airDensity, flow, situation, angularVelocity)
  }

  @Test
  def generalRudderTest() {
    val wingLocation = new Vector3f(0, 0, 0)
    val incidence = 0
    val dihedralDegree = 90
    val test = new SymmetricAirfoil("VerticalStabilizer", wingLocation, wingArea, incidence, aspectRatio, 
      true, dihedralDegree)
    val flow = new Quaternion().fromAngles(0, 0, 0).mult(Vector3f.UNIT_Z)
      .mult(300)
      .negate()
    val situation = new Quaternion().fromAngles(10, 0, 0)
    val angularVelocity = Vector3f.ZERO
    test.calculateResultantForce(airDensity, flow, situation, angularVelocity)
  }
}
