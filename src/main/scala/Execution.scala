package nanshan

import chisel3._
import chisel3.util._
import nanshan.Constant._

class Execution extends Module {
  val io = IO(new Bundle {
    val uop = Input(new MicroOp())
    val rs1_data = Input(UInt(64.W))
    val rs2_data = Input(UInt(64.W))
    val out = Output(UInt(64.W))
    val out_valid = Output(Bool())
    val next_pc = Output(UInt(32.W))
    val dmem = Flipped(new RamIO)
  })

 

  // printf("pc=%x, mem_c=%x, addr=%x, stall=%x, rdata=%x, wmask=%x, wdata=%x, wen=%x\n", 
  //         uop.pc, uop.mem_code, ls_addr, stall, io.dmem.rdata, io.dmem.wmask, io.dmem.wdata, io.dmem.wen)
}
