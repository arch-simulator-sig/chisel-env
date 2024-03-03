package nanshan

import chisel3._
import circt.stage.{CIRCTTarget, CIRCTTargetAnnotation, FirtoolOption}

object TopMain extends App {
  def parseArgs(info: String, args: Array[String]): String = {
    var target = ""
    for (arg <- args) { if (arg.startsWith(info + "=") == true) { target = arg } }
    require(target != "")
    target.substring(info.length() + 1)
  }

  chisel3.emitVerilog(
    new SimTop,
    args,
    Seq(
      FirtoolOption("--split-verilog"),
      FirtoolOption("-o=" + parseArgs("-td", args))
    )
  )
}
