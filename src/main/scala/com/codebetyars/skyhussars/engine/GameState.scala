package com.codebetyars.skyhussars.engine

//remove if not needed

abstract class GameState {

  def update(tpf: Float): GameState

  def close(): Unit

  def initialize(): Unit
}
