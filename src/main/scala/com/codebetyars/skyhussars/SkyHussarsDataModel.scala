package com.codebetyars.skyhussars

import com.codebetyars.skyhussars.engine.SoundManager._
import com.jme3.math.Vector3f

object SkyHussarsDataModel {

  val Gravity = new Vector3f(0f, -9.81f, 0f)

  case class BulletDescriptor(name: String, damage: Float)

  case class GunDescriptor(name: String, rateOfFire: Float, bullet: BulletDescriptor, muzzleVelocity: Float, spread: Float, sound: SoundDescriptor)
  case class GunLocationDescriptor(gun: GunDescriptor, roundsMax: Int, location: Vector3f)
  case class GunGroupDescriptor(name: String, gunLocations: List[GunLocationDescriptor])

  case class EngineDescriptor(name: String, thrustMax: Int, sound: SoundDescriptor)
  case class EngineLocation(engine: EngineDescriptor, location: Vector3f)

  case class PlaneDescriptor(
    name: String, engines: List[EngineLocation], guns: List[GunGroupDescriptor],
    massEmpty: Float, massTakeOffMax: Float, massGross: Float, internalTank: Float,
    length: Float, wingArea: Float, wingRatio: Float, dragFactor: Float)

  case class PlaneMissionDescriptor(plane: PlaneDescriptor, isPlayer: Boolean, startLocation: Vector3f, initialSpeed: Float)

  case class MissionDescriptor(name: String, planes: List[PlaneMissionDescriptor])

  // Ammo
  object Cal50_BMG extends BulletDescriptor(".50 BMG", 1.0f)

  // Guns
  object M3_Browning extends GunDescriptor(".50 M3 Browning", rateOfFire = 20, Bullets(".50 BMG"), muzzleVelocity = 890, spread = 0.5f, Sounds.Guns.Browning)

  // Engines
  object Allison_J33_A_9 extends EngineDescriptor("Allison J33-A-9", 17125, Sounds.Engines.Allison)
  object Allison_J33_A_17 extends EngineDescriptor("Allison J33-A-17", 17792, Sounds.Engines.Allison)

  // Planes
  object Lockheed_P_80A_1_LO_Shooting_Star extends PlaneDescriptor("Lockheed P-80A-1-LO Shooting Star",
    engines = List(EngineLocation(Allison_J33_A_9, new Vector3f(0f, 0f, 0f))),
    guns = List(GunGroupDescriptor("6x .50 M3 Browning", List(
      GunLocationDescriptor(M3_Browning, 300, new Vector3f(0.5f, 0.0f, 2.0f)),
      GunLocationDescriptor(M3_Browning, 300, new Vector3f(-0.5f, 0.0f, 2.0f))
    ))),
    massEmpty =  3593,
    massTakeOffMax =  6350,
    massGross = 5307,
    internalTank = 1609,
    length = 10.49f,
    wingArea = 22.07f,
    wingRatio = 6.37f,
    dragFactor = 0.2566f
  )

  // ...lists
  val Bullets: Map[String, BulletDescriptor] = Map(
    ".50 BMG" -> Cal50_BMG
  )

  val Guns: Map[String, GunDescriptor] = Map(
    ".50 M3 Browning" -> M3_Browning
  )

  val Engines: Map[String, EngineDescriptor] = Map(
    "Allison J33-A-9" -> Allison_J33_A_9,
    "Allison J33-A-17" -> Allison_J33_A_17
  )

  val Planes: Map[String, PlaneDescriptor] = Map(
    "Lockheed P-80A-1-LO Shooting Star" -> Lockheed_P_80A_1_LO_Shooting_Star
  )

  val Missions: Map[String, MissionDescriptor] = Map(
    "Test mission" -> MissionDescriptor("Test mission", List(
      PlaneMissionDescriptor(Lockheed_P_80A_1_LO_Shooting_Star, isPlayer = true, new Vector3f(0, 3000, 0), 300f),
      PlaneMissionDescriptor(Lockheed_P_80A_1_LO_Shooting_Star, isPlayer = false, new Vector3f(0, 3000, 100), 300f)
    ))
  )

}
