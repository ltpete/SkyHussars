package com.codebetyars.skyhussars.engine

abstract class GameState {

  def update(tpf: Float): GameState

  def close(): Unit

  def initialize(): Unit
}
