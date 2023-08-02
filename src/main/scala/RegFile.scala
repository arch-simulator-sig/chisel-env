package nanshan

import chisel3._
import difftest._

class RegFile extends Module {
  val io = IO(new Bundle {
    val rs1_addr = Input(UInt(5.W))
    val rs2_addr = Input(UInt(5.W))
    val rs1_data = Output(UInt(64.W))
    val rs2_data = Output(UInt(64.W))
    val rd_addr = Input(UInt(5.W))
    val rd_data = Input(UInt(64.W))
    val rd_en = Input(Bool())
    val trap_code = Output(UInt(3.W))
  })


}
