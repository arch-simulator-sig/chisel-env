package nanshan

import chisel3._
import chisel3.util._

class InstFetch extends Module {
  val io = IO(new Bundle {
    val imem = new RomIO
    val pc = Output(UInt(32.W))
    val inst = Output(UInt(32.W))
  })

  val pc_en = RegInit(false.B)
  pc_en := true.B

  val pc = RegInit("h80000000".U(32.W))
  pc := pc + 4.U

  io.imem.en := true.B
  io.imem.addr := pc.asUInt()

  io.pc := Mux(pc_en, pc, 0.U)
  io.inst := Mux(pc_en, io.imem.rdata(31, 0), 0.U)
}
