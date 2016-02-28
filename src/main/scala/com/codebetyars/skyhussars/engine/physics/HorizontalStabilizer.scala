package com.codebetyars.skyhussars.engine.physics

import com.jme3.math.Vector3f
//remove if not needed

class HorizontalStabilizer(name: String, 
    cog: Vector3f, 
    wingArea: Float, 
    incidence: Float, 
    aspectRatio: Float) extends SymmetricAirfoil(name, cog, wingArea, incidence, aspectRatio, false, 
  90f)
