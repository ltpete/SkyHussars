package com.codebetyars.skyhussars.engine

//remove if not needed

object StateName extends Enumeration {

  val MAIN_MENU = new StateName()

  val LEVEL = new StateName()

  class StateName extends Val

  implicit def convertValue(v: Value): StateName = v.asInstanceOf[StateName]
}
