package com.codebetyars.skyhussars.engine

trait GameState {

  def initialize(): Unit

  def update(tpf: Float): GameState

  def close(): Unit

}
