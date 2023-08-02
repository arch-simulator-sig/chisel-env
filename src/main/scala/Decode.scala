package nanshan

import chisel3._
import chisel3.util._
import nanshan.Constant._
import nanshan.Instructions._
import chisel3.experimental.ChiselEnum


class Decode extends Module {
  val io = IO(new Bundle {
    val pc = Input(UInt(32.W))
    val inst = Input(UInt(32.W))
    val uop = Output(new MicroOp())
  })

  val inst = io.inst
  
  val ctrl = ListLookup(inst,
                //   v  fu_code alu_code  jmp_code  mem_code mem_size   w  rs1_src       rs2_src  rd_en  imm_type  
                List(N, FU_X,   ALU_X,    JMP_X,    MEM_X,   MEM_X,     N, RS_X,         RS_X,        N, IMM_X     ), 
    Array(
      // RV32I
      LUI   ->  List(Y, FU_ALU, ALU_ADD,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_ZERO, RS_FROM_IMM, Y, IMM_U     ),
      AUIPC ->  List(Y, FU_ALU, ALU_ADD,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_PC,   RS_FROM_IMM, Y, IMM_U     ),
      JAL   ->  List(Y, FU_JMP, ALU_X,    JMP_JAL,  MEM_X,   MEM_X,     N, RS_FROM_PC,   RS_FROM_IMM, Y, IMM_J     ),
      JALR  ->  List(Y, FU_JMP, ALU_X,    JMP_JALR, MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_I     ),
      BEQ   ->  List(Y, FU_JMP, ALU_X,    JMP_BEQ,  MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_B     ),
      BNE   ->  List(Y, FU_JMP, ALU_X,    JMP_BNE,  MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_B     ),
      BLT   ->  List(Y, FU_JMP, ALU_X,    JMP_BLT,  MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_B     ),
      BGE   ->  List(Y, FU_JMP, ALU_X,    JMP_BGE,  MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_B     ),
      BLTU  ->  List(Y, FU_JMP, ALU_X,    JMP_BLTU, MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_B     ),
      BGEU  ->  List(Y, FU_JMP, ALU_X,    JMP_BGEU, MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_B     ),
      LB    ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_LD,  MEM_BYTE,  N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      LH    ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_LD,  MEM_HALF,  N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      LW    ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_LD,  MEM_WORD,  N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      LBU   ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_LDU, MEM_BYTE,  N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      LHU   ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_LDU, MEM_HALF,  N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      SB    ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_ST,  MEM_BYTE,  N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_S     ),
      SH    ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_ST,  MEM_HALF,  N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_S     ),
      SW    ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_ST,  MEM_WORD,  N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_S     ),
      ADDI  ->  List(Y, FU_ALU, ALU_ADD,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      SLTI  ->  List(Y, FU_ALU, ALU_SLT,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      SLTIU ->  List(Y, FU_ALU, ALU_SLTU, JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      XORI  ->  List(Y, FU_ALU, ALU_XOR,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      ORI   ->  List(Y, FU_ALU, ALU_OR,   JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      ANDI  ->  List(Y, FU_ALU, ALU_AND,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      SLLI  ->  List(Y, FU_ALU, ALU_SLL,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_SHAMT ),
      SRLI  ->  List(Y, FU_ALU, ALU_SRL,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_SHAMT ),
      SRAI  ->  List(Y, FU_ALU, ALU_SRA,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_SHAMT ),
      ADD   ->  List(Y, FU_ALU, ALU_ADD,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SUB   ->  List(Y, FU_ALU, ALU_SUB,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SLL   ->  List(Y, FU_ALU, ALU_SLL,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SLT   ->  List(Y, FU_ALU, ALU_SLT,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SLTU  ->  List(Y, FU_ALU, ALU_SLTU, JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      XOR   ->  List(Y, FU_ALU, ALU_XOR,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SRL   ->  List(Y, FU_ALU, ALU_SRL,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SRA   ->  List(Y, FU_ALU, ALU_SRA,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      OR    ->  List(Y, FU_ALU, ALU_OR,   JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      AND   ->  List(Y, FU_ALU, ALU_AND,  JMP_X,    MEM_X,   MEM_X,     N, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      // FENCE
      // ECALL
      // EBREAK
      // RV64I
      LWU   ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_LDU, MEM_WORD,  N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      LD    ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_LDU, MEM_DWORD, N, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      SD    ->  List(Y, FU_MEM, ALU_X,    JMP_X,    MEM_ST,  MEM_DWORD, N, RS_FROM_RF,   RS_FROM_RF,  N, IMM_S     ),
      ADDIW ->  List(Y, FU_ALU, ALU_ADD,  JMP_X,    MEM_X,   MEM_X,     Y, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      SLLIW ->  List(Y, FU_ALU, ALU_SLL,  JMP_X,    MEM_X,   MEM_X,     Y, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      SRLIW ->  List(Y, FU_ALU, ALU_SRL,  JMP_X,    MEM_X,   MEM_X,     Y, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      SRAIW ->  List(Y, FU_ALU, ALU_SRA,  JMP_X,    MEM_X,   MEM_X,     Y, RS_FROM_RF,   RS_FROM_IMM, Y, IMM_I     ),
      ADDW  ->  List(Y, FU_ALU, ALU_ADD,  JMP_X,    MEM_X,   MEM_X,     Y, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SUBW  ->  List(Y, FU_ALU, ALU_SUB,  JMP_X,    MEM_X,   MEM_X,     Y, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SLLW  ->  List(Y, FU_ALU, ALU_SLL,  JMP_X,    MEM_X,   MEM_X,     Y, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SRLW  ->  List(Y, FU_ALU, ALU_SRL,  JMP_X,    MEM_X,   MEM_X,     Y, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      SRAW  ->  List(Y, FU_ALU, ALU_SRA,  JMP_X,    MEM_X,   MEM_X,     Y, RS_FROM_RF,   RS_FROM_RF,  Y, IMM_X     ),
      // nemu
      HALT  ->  List(Y, FU_X,   ALU_X,    JMP_X,    MEM_X,   MEM_X,     N, RS_X,         RS_X,        N, IMM_X     )
    )
  )

  object CtrlSignal extends ChiselEnum {
  val ctrl_valid, fu_code, alu_code, jmp_code, mem_code, mem_size,w_type, rs1_src, rs2_src, rd_en, imm_type = ctrl
}

}
