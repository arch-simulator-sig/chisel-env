// import Mill dependency
import mill._
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule
import mill.scalalib.TestModule.Utest
// support BSP
import mill.bsp._

object nanshan extends ScalaModule with ScalafmtModule { m =>
  override def millSourcePath = os.pwd
  override def scalaVersion   = "2.13.12"
  override def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit"
  )

  def sources = T.sources {
    super.sources() ++ Seq(
      PathRef(millSourcePath / "difftest"),
      PathRef(millSourcePath / "rvdecoderdb" / "rvdecoderdb")
    )
  }

  def resources = T.sources {
    super.resources() ++ Seq(
      PathRef(millSourcePath / "src" / "main" / "resources")
    )
  }

  override def ivyDeps = Agg(
    ivy"org.chipsalliance::chisel:7.0.0-M1"
  )

  override def scalacPluginIvyDeps = Agg(
    ivy"org.chipsalliance:::chisel-plugin:7.0.0-M1"
  )

  object test extends ScalaTests with TestModule.ScalaTest {
    override def ivyDeps = m.ivyDeps() ++ Agg(
      ivy"org.scalatest::scalatest::3.2.16"
    )
  }

  def repositoriesTask = T.task {
    Seq(
      coursier.MavenRepository("http://mirrors.cloud.tencent.com/nexus/repository/maven-public"),
      coursier.MavenRepository(
        "https://repo.scala-sbt.org/scalasbt/maven-releases"
      )
    ) ++ super.repositoriesTask()
  }
}
