package nanshan
import chisel3._
import chisel3.util._
import difftest._

class SimTop extends Module {
  val io = IO(new Bundle {
    val logCtrl  = new LogCtrlIO
    val perfInfo = new PerfInfoIO
    val uart     = new UARTIO
  })

  val core = Module(new Core)

  val mem = Module(new Ram2r1w)
  mem.io.imem <> core.io.imem
  mem.io.dmem <> core.io.dmem

  io.uart.out.valid := false.B
  io.uart.out.ch    := 0.U
  io.uart.in.valid  := false.B

}
