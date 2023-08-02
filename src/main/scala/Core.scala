package nanshan

import chisel3._
import difftest._

class Core extends Module {
  val io = IO(new Bundle {
    val imem = Flipped(new RomIO)
    val dmem = Flipped(new RamIO)
  })



}
