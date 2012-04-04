package com.thoughtworks.scaluxt

trait UxTesting {

  val usabilityTestConfig = TestConfig()
  def isUxVersionFor(nameOfTest:String, versionName:String) = currentVersionFor(nameOfTest) == versionName
  def withUxVersionFor(nameOfTest:String, versionName:String)(block: => Any) = {
	if(isUxVersionFor(nameOfTest, versionName)) {
      block
    }
  }
  def trackEvent(testName:String, versionName:String, eventName:String) {
	usabilityTestConfig.getVersionsForTest(testName)
					   .find(_.versionName == versionName)
    				   .get
					   .trackEvent(eventName)
  }
  def currentVersionFor(testName:String) = ""
  def getNextValueFor(testName:String) = usabilityTestConfig.getNextTestVersionFor(testName)

}