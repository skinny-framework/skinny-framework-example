// ------------------------------
// Automated code formatter before compilaion
// Disabled by default because this is confusing for beginners
scalariformSettings

// ------------------------------
// for ./skinny test:coverage
// https://github.com/scoverage/sbt-scoverage
instrumentSettings
org.scoverage.coveralls.CoverallsPlugin.coverallsSettings
ScoverageKeys.highlighting := true

// ------------------------------
// for ./skinnny console
initialCommands := """
import skinny._
import _root_.controller._, model._
import org.joda.time._
import scalikejdbc._, config._
DBSettings.initialize()
"""

// ------------------------------
// sbt-dependency-graph
//net.virtualvoid.sbt.graph.Plugin.graphSettings

