package nanshan

import circt.stage._

object TopMain extends App {
  (new ChiselStage).execute(args, Seq(chisel3.stage.ChiselGeneratorAnnotation(() => new SimTop)) :+ CIRCTTargetAnnotation(CIRCTTarget.Verilog))
}
