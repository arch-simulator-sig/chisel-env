package nanshan

import circt.stage._

object TopMain extends App {
  def parseArgs(info: String, args: Array[String]): String = {
    var target = ""
    for (arg <- args) { if (arg.startsWith(info + "=") == true) { target = arg } }
    require(target != "")
    target.substring(info.length() + 1)
  }

  (new ChiselStage).execute(
    args,
    Seq(chisel3.stage.ChiselGeneratorAnnotation(() => new SimTop)) :+ CIRCTTargetAnnotation(
      CIRCTTarget.Verilog
    ) :+ FirtoolOption("--split-verilog") :+ FirtoolOption("-o=" + parseArgs("-td", args))
  )
}
