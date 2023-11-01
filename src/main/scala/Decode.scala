package nanshan

import chisel3._
import chisel3.util._
import Instructions._

class Decode extends Module {
  val io = IO(new Bundle {
    val inst     = Input(UInt(32.W))
    val rs1_addr = Output(UInt(5.W))
    val rs1_en   = Output(Bool())
    val rs2_addr = Output(UInt(5.W))
    val rs2_en   = Output(Bool())
    val rd_addr  = Output(UInt(5.W))
    val rd_en    = Output(Bool())
    val opcode   = Output(UInt(8.W))
    val imm      = Output(UInt(64.W))
  })

  val inst   = io.inst
  val opcode = WireInit(UInt(8.W), 0.U)
  val imm_i  = Cat(Fill(53, inst(31)), inst(30, 20))

  // Only example here, use your own control flow!
  when(inst === ADDI) {
    opcode := 1.U
  }

  io.rs1_addr := inst(19, 15)
  io.rs2_addr := inst(24, 20)
  io.rd_addr  := inst(11, 7)

  io.rs1_en := false.B
  io.rs2_en := false.B
  io.rd_en  := false.B

  when(inst === ADDI) {
    io.rs1_en := true.B
    io.rs2_en := false.B
    io.rd_en  := true.B
  }

  io.opcode := opcode
  io.imm    := imm_i
}
