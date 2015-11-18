resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("org.skinny-framework" % "sbt-servlet-plugin" % "2.0.2")
addSbtPlugin("org.skinny-framework" % "sbt-scalate-precompiler" % "1.7.1.0")
addSbtPlugin("com.typesafe.sbt"     % "sbt-scalariform" % "1.3.0")
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.9")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
