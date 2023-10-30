package nanshan

import chisel3._
import chisel3.util._

class RomIO extends Bundle {
  val en = Output(Bool())
  val addr = Output(UInt(64.W))
  val rdata = Input(UInt(64.W))
}

class RamIO extends RomIO {
  val wdata = Output(UInt(64.W))
  val wmask = Output(UInt(64.W))
  val wen = Output(Bool())
}

class ram_2r1w extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk = Input(Clock())
    val imem_en = Input(Bool())
    val imem_addr = Input(UInt(64.W))
    val imem_data = Output(UInt(32.W))
    val dmem_en = Input(Bool())
    val dmem_addr = Input(UInt(64.W))
    val dmem_rdata = Output(UInt(64.W))
    val dmem_wdata = Input(UInt(64.W))
    val dmem_wmask = Input(UInt(64.W))
    val dmem_wen = Input(Bool())
  })
  addResource("/vsrc/ram_2r1w.v")
}

class Ram2r1w extends Module {
  val io = IO(new Bundle {
    val imem = Flipped(new RomIO)
    val dmem = Flipped(new RamIO)
  })
  val mem = Module(new ram_2r1w)
  mem.io.clk        := clock
  mem.io.imem_en    := io.imem.en
  mem.io.imem_addr  := io.imem.addr
  io.imem.rdata     := mem.io.imem_data
  mem.io.dmem_en    := io.dmem.en
  mem.io.dmem_addr  := io.dmem.addr
  io.dmem.rdata     := mem.io.dmem_rdata
  mem.io.dmem_wdata := io.dmem.wdata
  mem.io.dmem_wmask := io.dmem.wmask
  mem.io.dmem_wen   := io.dmem.wen
}
