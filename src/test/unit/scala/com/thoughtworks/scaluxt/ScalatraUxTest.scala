package com.thoughtworks.scaluxt

import xml.Elem
import java.io.File
import org.scalatra.ScalatraFilter
import org.scalatra.scalate.ScalateSupport

import scala.collection.JavaConversions._
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext

object ScalatraUxTest extends UxTesting {
    
  def main(args: Array[String]) {
	val server = new Server(8881)
	val handler = new WebAppContext
	handler.setWar("./src/test/unit/webapp")
	server.setHandler(handler)
	server.start
  }  
}

class ScalatraUxTest extends ScalatraFilter 
					 with ScalateSupport 
					 with UxTestingScalatra  {
						
  override val usabilityTestConfig = TestConfig.fromXmlFile("file.txt")						
						
  get("/*") {
	if(isUxVersionFor("testXY", UxVersion.A)) println("Hey, that is test A")
	serveContent("Version " + currentVersionFor("testXY"))
  }

  get("/config") {
	val text = """<a href="/userevent/testXY/""" + currentVersionFor("testXY") + """/hello">click action</a>"""
    serveContent(text)
  }

  get("/userevent/:testName/:version/:eventName") {
    val testName = params("testName")
    val versionName = params("version")
    val eventName = params("eventName")
	
	trackEvent(testName, versionName, eventName)
	val text = """Clicked on Version: """ + versionName + """ for test """ + testName + """ and """ + eventName
    serveContent(text)
  }

  private def serveContent(bodyMessage: String) = {
    contentType = "text/html"
	"""<html><body>""" + bodyMessage + """</body></html>"""
  }
}
