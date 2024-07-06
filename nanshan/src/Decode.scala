package nanshan

import chisel3._
import chisel3.util._
import chisel3.util.experimental.decode._
import org.chipsalliance.rvdecoderdb

case class Insn(val inst: rvdecoderdb.Instruction) extends DecodePattern {
  override def bitPat: BitPat = BitPat("b" + inst.encoding.toString())
}

object Opcode extends DecodeField[Insn, UInt] {
  override def name = "opcode"

  override def chiselType = UInt(8.W)

  override def genTable(i: Insn): BitPat = i.inst.name match {
    case "add" => BitPat("b00000001") // or use ChiselEnum as well, this is just a demo
    case "sub" => BitPat("b00000010")
    case _     => BitPat("b00000000")
  }
}

//TODO: Use ChiselEnum1H after #2261 has been merged
object ImmTypeEnum extends ChiselEnum {
  val immNone, immI, immS, immB, immU, immJ, immShamtD, immShamtW = Value
}

object ImmType extends DecodeField[Insn, ImmTypeEnum.Type] {
  override def name = "imm_type"

  override def chiselType = ImmTypeEnum()

  override def genTable(i: Insn): BitPat = {
    val immType = i.inst.args
      .map(_.name match {
        case "imm12"                 => ImmTypeEnum.immI
        case "imm12hi" | "imm12lo"   => ImmTypeEnum.immS
        case "bimm12hi" | "bimm12lo" => ImmTypeEnum.immB
        case "imm20"                 => ImmTypeEnum.immU
        case "jimm20"                => ImmTypeEnum.immJ
        case "shamtd"                => ImmTypeEnum.immShamtD
        case "shamtw"                => ImmTypeEnum.immShamtW
        case _                       => ImmTypeEnum.immNone
      })
      .filterNot(_ == ImmTypeEnum.immNone)
      .headOption // different ImmType will not appear in the Seq
      .getOrElse(ImmTypeEnum.immNone)

    // TODO: BitPat will accept ChiselEnum after #2327 has been merged
    BitPat(immType.litValue.U((immType.getWidth).W))
  }

}

object Rs1En extends BoolDecodeField[Insn] {
  override def name: String = "rs1_en"

  override def genTable(i: Insn): BitPat = if (i.inst.args.map(_.name).contains("rs1")) y else n
}

object Rs2En extends BoolDecodeField[Insn] {
  override def name: String = "rs2_en"

  override def genTable(i: Insn): BitPat = if (i.inst.args.map(_.name).contains("rs2")) y else n
}

object RdEn extends BoolDecodeField[Insn] {
  override def name: String = "rd_en"

  override def genTable(i: Insn): BitPat = if (i.inst.args.map(_.name).contains("rd")) y else n
}
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
  val inst = io.inst

  val instTable  = rvdecoderdb.fromFile.instructions(os.pwd / "riscv-opcodes")
  val targetSets = Set("rv_i", "rv64_i", "rv_m", "rv64_m")
  // add implemented instructions here
  val instList = instTable
    .filter(instr => targetSets.contains(instr.instructionSet.name))
    .filter(_.pseudoFrom.isEmpty)
    .map(Insn(_))
    .toSeq

  val decodeTable   = new DecodeTable(instList, Seq(Opcode, ImmType, Rs1En, Rs2En, RdEn))
  val decodedBundle = decodeTable.decode(inst)

  val imm_i      = Cat(Fill(52, inst(31)), inst(31, 20))                              // I-type
  val imm_s      = Cat(Fill(52, inst(31)), inst(31, 25), inst(11, 7))                 // S-type
  val imm_b      = Cat(Fill(52, inst(31)), inst(7), inst(30, 25), inst(11, 8), 0.U)   // B-type
  val imm_u      = Cat(Fill(32, inst(31)), inst(31, 12), Fill(12, 0.U))               // U-type
  val imm_j      = Cat(Fill(44, inst(31)), inst(19, 12), inst(20), inst(30, 21), 0.U) // J-type
  val imm_shamtd = Cat(Fill(58, 0.U), inst(25, 20))
  val imm_shamtw = Cat(Fill(59, 0.U), inst(24, 20))
  io.opcode := decodedBundle(Opcode)

  val imm_type = decodedBundle(ImmType)
  // TODO: Use Mux1H after #2261 has been merged
  io.imm := MuxLookup(imm_type, 0.U)(
    Seq(
      ImmTypeEnum.immI      -> imm_i,
      ImmTypeEnum.immS      -> imm_s,
      ImmTypeEnum.immB      -> imm_b,
      ImmTypeEnum.immU      -> imm_u,
      ImmTypeEnum.immJ      -> imm_j,
      ImmTypeEnum.immShamtD -> imm_shamtd,
      ImmTypeEnum.immShamtW -> imm_shamtw
    )
  )

  io.rs1_en := decodedBundle(Rs1En)
  io.rs2_en := decodedBundle(Rs2En)
  io.rd_en  := decodedBundle(RdEn)

  io.rs1_addr := inst(19, 15)
  io.rs2_addr := inst(24, 20)
  io.rd_addr  := inst(11, 7)

}
