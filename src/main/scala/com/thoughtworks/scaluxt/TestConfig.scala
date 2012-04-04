package com.thoughtworks.scaluxt

import scala.xml._

object TestConfig {
	def apply(testName: String) = {
		new TestConfig(List(UxVersion.A, UxVersion.B).map(UxVersion(testName, _, 50)))
	}
	
	def apply() = new TestConfig(List())
	def fromXmlFile(fileName:String) = {
		val lines = scala.io.Source.fromFile(fileName).mkString
		val xmlConfig = XML.loadString(lines)
		val versionItems = (xmlConfig \ "TestConfig").map { testConfig =>
			val name = (testConfig \ "@name").head.text
			(testConfig \ "TestVersion").map { version =>
					val versionName = (version \ "@name").head.text
					val percentage = (version \ "@percentage").head.text.toInt					
					UxVersion(name, versionName, percentage)
			}
			
		}.flatten

		new TestConfig(versionItems.toList)
	}
}

class TestConfig(val versionItems:List[UxVersion] = List()) {
	def getNextTestVersionFor(testName:String) = {
		val version = findVersionFor(currentPercentageValue, getVersionsForTest(testName))
		version.incVersionUsage
		version.versionName
	}
	
	def getVersionsForTest(testName:String) = versionItems.filter(_.testName == testName)
	
	private def currentPercentageValue = (Math.random * 100).toInt
	private def findVersionFor(percentage:Int, versionItems:List[UxVersion]) = {
		var max = 0
		versionItems.map(version => {
					max += version.percentage
					(max, version) })
		   .find(_._1 >= percentage)
		   .get._2
	}
}