package nanshan

import chisel3._
import chisel3.util.experimental._
import difftest._

class Core extends Module {
  val io = IO(new Bundle {
    val imem = new RomIO
    val dmem = new RamIO
  })

  val fetch = Module(new InstFetch)
  fetch.io.imem <> io.imem

  val decode = Module(new Decode)
  decode.io.inst := fetch.io.inst

  val rf = Module(new RegFile)
  rf.io.rs1_addr := decode.io.rs1_addr
  rf.io.rs2_addr := decode.io.rs2_addr
  rf.io.rd_addr  := decode.io.rd_addr
  rf.io.rd_en    := decode.io.rd_en

  val execution = Module(new Execution)
  execution.io.opcode := decode.io.opcode
  execution.io.in1    := Mux(decode.io.rs1_en, rf.io.rs1_data, 0.U)
  execution.io.in2    := Mux(decode.io.rs2_en, rf.io.rs2_data, decode.io.imm)
  execution.io.dmem   <> io.dmem
  rf.io.rd_data       := execution.io.out

  /* ----- Difftest ------------------------------ */

  val dt_ic = Module(new DifftestInstrCommit)
  dt_ic.io.clock    := clock
  dt_ic.io.coreid   := 0.U
  dt_ic.io.index    := 0.U
  dt_ic.io.valid    := true.B
  dt_ic.io.pc       := fetch.io.pc
  dt_ic.io.instr    := fetch.io.inst
  dt_ic.io.skip     := false.B
  dt_ic.io.isRVC    := false.B
  dt_ic.io.scFailed := false.B
  dt_ic.io.wen      := decode.io.rd_en
  dt_ic.io.wdata    := execution.io.out
  dt_ic.io.wdest    := decode.io.rd_addr

  val dt_ae = Module(new DifftestArchEvent)
  dt_ae.io.clock       := clock
  dt_ae.io.coreid      := 0.U
  dt_ae.io.intrNO      := 0.U
  dt_ae.io.cause       := 0.U
  dt_ae.io.exceptionPC := 0.U

  val cycle_cnt = RegInit(0.U(64.W))
  val instr_cnt = RegInit(0.U(64.W))

  cycle_cnt := cycle_cnt + 1.U
  instr_cnt := instr_cnt + 1.U

  val rf_a0 = WireDefault(0.U(64.W))
  rf_a0 := BoringUtils.bore(rf.rf(10))

  val dt_te = Module(new DifftestTrapEvent)
  dt_te.io.clock    := clock
  dt_te.io.coreid   := 0.U
  dt_te.io.valid    := (fetch.io.inst === "h0000006b".U)
  dt_te.io.code     := rf_a0(2, 0)
  dt_te.io.pc       := fetch.io.pc
  dt_te.io.cycleCnt := cycle_cnt
  dt_te.io.instrCnt := instr_cnt

  val dt_cs = Module(new DifftestCSRState)
  dt_cs.io.clock          := clock
  dt_cs.io.coreid         := 0.U
  dt_cs.io.priviledgeMode := 3.U // Machine mode
  dt_cs.io.mstatus        := 0.U
  dt_cs.io.sstatus        := 0.U
  dt_cs.io.mepc           := 0.U
  dt_cs.io.sepc           := 0.U
  dt_cs.io.mtval          := 0.U
  dt_cs.io.stval          := 0.U
  dt_cs.io.mtvec          := 0.U
  dt_cs.io.stvec          := 0.U
  dt_cs.io.mcause         := 0.U
  dt_cs.io.scause         := 0.U
  dt_cs.io.satp           := 0.U
  dt_cs.io.mip            := 0.U
  dt_cs.io.mie            := 0.U
  dt_cs.io.mscratch       := 0.U
  dt_cs.io.sscratch       := 0.U
  dt_cs.io.mideleg        := 0.U
  dt_cs.io.medeleg        := 0.U
}
