// import mill dependency
import mill._
import mill.scalalib._
import mill.scalalib.TestModule.Utest
// support BSP
import mill.bsp._
// maven repository
import coursier.maven.MavenRepository

object ivys {
  val sv = "2.12.16"
  val chisel3 = ivy"edu.berkeley.cs::chisel3:3.5.5"
  val chisel3Plugin = ivy"edu.berkeley.cs:::chisel3-plugin:3.5.5"
  val chiseltest = ivy"edu.berkeley.cs::chiseltest:0.3.3"
  val scalatest = ivy"org.scalatest::scalatest:3.2.14"
  val macroParadise = ivy"org.scalamacros:::paradise:2.1.1"
}

trait CommonModule extends ScalaModule {
  override def scalaVersion = ivys.sv
  override def scalacOptions = Seq("-Xsource:2.11")
  override def compileIvyDeps = Agg(ivys.macroParadise)
  override def scalacPluginIvyDeps = Agg(ivys.macroParadise, ivys.chisel3Plugin)
}

object difftest extends SbtModule with CommonModule {
  override def millSourcePath = os.pwd / "difftest"
  override def ivyDeps = super.ivyDeps() ++ Agg(ivys.chisel3)
}

object nanshan extends SbtModule with CommonModule {
  override def millSourcePath = os.pwd
  override def ivyDeps = super.ivyDeps() ++ Agg(ivys.chisel3)
  override def moduleDeps = super.moduleDeps ++ Seq(
    difftest
  )
}



// 船新chisel 6.0 版本
// import Mill dependency
// import mill._
// import mill.define.Sources
// import mill.modules.Util
// import mill.scalalib.scalafmt.ScalafmtModule
// import $ivy.`com.lihaoyi::mill-contrib-bloop:`
// import scalalib._
// // support BSP
// import mill.bsp._

// trait BaseScalaModule extends ScalaModule with ScalafmtModule {
//   override def millSourcePath = os.pwd
//   override def scalaVersion = "2.13.10"
//   override def scalacOptions = Seq(
//     "-language:reflectiveCalls",
//     "-deprecation",
//     "-feature",
//     "-Xcheckinit"
//   )
//   override def ivyDeps = Agg(
//     ivy"org.chipsalliance::chisel:6.0.0-M2"
//   )
//   override def scalacPluginIvyDeps = Agg(
//     ivy"org.chipsalliance:::chisel-plugin:6.0.0-M3",
//   )
// }

// object difftest extends BaseScalaModule {
//   override def millSourcePath = os.pwd / "difftest"
// //   override def ivyDeps = Agg(
// //     ivy"edu.berkeley.cs::chisel3:3.5.6",
// //     ivy"edu.berkeley.cs:::chisel3-plugin:3.5.6")
// }

// object nanshan extends BaseScalaModule  {
//   override def millSourcePath = os.pwd
// //   override def scalaVersion = "2.13.10"
// //   override def scalacOptions = Seq(
// //     "-language:reflectiveCalls",
// //     "-deprecation",
// //     "-feature",
// //     "-Xcheckinit",
// //     "-P:chiselplugin:genBundleElements"
// //   )

// //   override def ivyDeps = Agg(
// //     ivy"edu.berkeley.cs::chisel3:3.5.6",
// // 	ivy"ch.epfl.scala::bloop-config:1.5.5"

// //   )
//   override def scalacPluginIvyDeps = Agg(
//     ivy"edu.berkeley.cs:::chisel3-plugin:3.5.6",
//   )
//   override def moduleDeps = super.moduleDeps ++ Seq(
//     difftest
//   )
// }