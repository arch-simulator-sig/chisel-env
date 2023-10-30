package nanshan

import chisel3._
import chisel3.util._

class Execution extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(8.W))
    val in1 = Input(UInt(64.W))
    val in2 = Input(UInt(64.W))
    val out = Output(UInt(64.W))
    val dmem = new RamIO
  })

  io.out := 0.U

  // ADDI
  when (io.opcode === 1.U) {
    io.out := io.in1 + io.in2
  }

  io.dmem.en := false.B
  io.dmem.addr := 0.U
  io.dmem.wen := false.B
  io.dmem.wdata := 0.U
  io.dmem.wmask := 0.U

}
