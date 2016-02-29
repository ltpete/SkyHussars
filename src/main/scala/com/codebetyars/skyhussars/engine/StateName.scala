package com.codebetyars.skyhussars.engine



object StateName extends Enumeration {

  val MAIN_MENU = new StateName()

  val LEVEL = new StateName()

  class StateName extends Val

  implicit def convertValue(v: Value): StateName = v.asInstanceOf[StateName]
}
