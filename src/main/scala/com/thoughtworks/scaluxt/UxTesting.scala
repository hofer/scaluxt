package com.thoughtworks.scaluxt

trait UxTesting {

  val usabilityTestConfig = TestConfig()
  def isUxVersionFor(nameOfTest:String, versionName:String) = currentVersionFor(nameOfTest) == versionName
  def withUxVersionFor(nameOfTest:String, versionName:String)(block: => Any) = {
	if(isUxVersionFor(nameOfTest, versionName)) {
      block
    }
  }
  def trackEvent(testName:String, eventName:String) {
    val versionName = currentVersionFor(testName)
	  usabilityTestConfig.getVersionsForTest(testName)
					             .find(_.versionName == versionName)
    				           .get
					             .trackEvent(eventName)
  }

  def trackUniqueEvent(testName:String, eventName:String, value:String = "") {
    val versionName = currentVersionFor(testName)
    if(!checkAndSetUniqueEvent(testName, versionName, eventName, value)) {
      trackEvent(testName:String, eventName:String)
    }
  }
  def currentVersionFor(testName:String) = ""
  def checkAndSetUniqueEvent(testName:String, versionName:String, eventName:String, value:String) = false
  def getNextValueFor(testName:String) = usabilityTestConfig.getNextTestVersionFor(testName)

}
