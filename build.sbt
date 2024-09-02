ThisBuild / scalaVersion     := "2.12.19"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.sick.child"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "sick-child-app",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.6",
      "io.getquill"          %% "quill-jdbc-zio" % "4.8.4",
      "org.postgresql"       %  "postgresql"     % "42.3.1",
      "dev.zio" %% "zio-streams" % "2.1.6",
      "dev.zio" %% "zio-test" % "2.1.6" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
