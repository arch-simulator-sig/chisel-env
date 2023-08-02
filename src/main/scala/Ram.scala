package nanshan

import chisel3._
import chisel3.util._

class RAMHelper extends BlackBox {
  val io = IO(new Bundle {
    val clk   = Input(Clock())
    val en    = Input(Bool())
    val rIdx  = Input(UInt(64.W))
    val rdata = Output(UInt(64.W))
    val wIdx  = Input(UInt(64.W))
    val wdata = Input(UInt(64.W))
    val wmask = Input(UInt(64.W))
    val wen   = Input(Bool())
  })
}

class ram_2r1w extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk        = Input(Clock())
    val imem_en    = Input(Bool())
    val imem_addr  = Input(UInt(64.W))
    val imem_data  = Output(UInt(32.W))
    val dmem_en    = Input(Bool())
    val dmem_addr  = Input(UInt(64.W))
    val dmem_rdata = Output(UInt(64.W))
    val dmem_wdata = Input(UInt(64.W))
    val dmem_wmask = Input(UInt(64.W))
    val dmem_wen   = Input(Bool())
  })
  addResource("/vsrc/ram_2r1w.v")
}

class ROMIO extends Bundle {
  val addr = Input(UInt(64.W))
  val data = Input((UInt(32.W)))
  val en   = Output(Bool())
}

class RAMIO extends ROMIO {
  val wdata = Output(UInt(64.W))
  val wmask = Output(UInt(64.W))
  val wen   = Output(Bool())
}

class Ram2r1w extends Module {
  val io = IO(new Bundle {
    val rom = new ROMIO
    val ram = new RAMIO
  })

  val mem = Module(new ram_2r1w)

  val rom = io.rom
  val ram = io.ram

  mem.io.clk := Clock()
  mem.io.imem_en := rom.en
  mem.io.imem_addr := rom.addr
  rom.data := mem.io.imem_data

  mem.io.dmem_addr := ram.addr
  mem.io.dmem_en := ram.en
  mem.io.dmem_wdata := ram.wdata
  mem.io.dmem_wmask := ram.wmask
  mem.io.dmem_wen := ram.wen
  ram.data := mem.io.dmem_rdata
}
