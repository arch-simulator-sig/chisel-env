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
  override def scalacOptions  = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit"
  )

  def sources = T.sources {
    super.sources() ++ Seq(PathRef(millSourcePath / "difftest"))
  }

  def resources = T.sources {
    super.resources() ++ Seq(
      PathRef(millSourcePath / "src" / "main" / "resources")
    )
  }

  override def ivyDeps = Agg(
    ivy"org.chipsalliance::chisel:6.0.0-RC2"
  )

  override def scalacPluginIvyDeps = Agg(
    ivy"org.chipsalliance:::chisel-plugin:6.0.0-RC2"
  )

  object test extends ScalaTests with Utest {
    override def ivyDeps = m.ivyDeps() ++ Agg(
      ivy"com.lihaoyi::utest:0.8.1",
      ivy"edu.berkeley.cs::chiseltest:5.0.0"
    )
  }

  def repositoriesTask = T.task {
    Seq(
      coursier.MavenRepository("https://maven.aliyun.com/repository/central"),
      coursier.MavenRepository(
        "https://repo.scala-sbt.org/scalasbt/maven-releases"
      )
    ) ++ super.repositoriesTask()
  }
}
