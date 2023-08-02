/**************************************************************************************
* Copyright (c) 2021 Li Shi
*
* nanshan is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*             http://license.coscl.org.cn/MulanPSL2
*
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR
* FIT FOR A PARTICULAR PURPOSE.
*
* See the Mulan PSL v2 for more details.
***************************************************************************************/

package nanshan

object TopMain extends App {

  if (nanshanConfig.EnableDifftest) {
    (new chisel3.stage.ChiselStage).execute(args, Seq(
      chisel3.stage.ChiselGeneratorAnnotation(() => new SimTop())
    ))
  } else {
    if (nanshanConfig.TargetOscpuSoc) {
      (new chisel3.stage.ChiselStage).execute(args, Seq(
        chisel3.stage.ChiselGeneratorAnnotation(() => new RealTop()),
        firrtl.stage.RunFirrtlTransformAnnotation(new AddModulePrefix()),
        ModulePrefixAnnotation(s"ysyx_${nanshanConfig.OscpuId}_")
      ))
    } else {
      (new chisel3.stage.ChiselStage).execute(args, Seq(
        chisel3.stage.ChiselGeneratorAnnotation(() => new RealTop())
      ))
    }
  }

}
