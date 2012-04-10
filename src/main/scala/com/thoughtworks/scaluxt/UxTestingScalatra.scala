package com.thoughtworks.scaluxt

import org.scalatra._
import javax.servlet.http.Cookie

trait UxTestingScalatra extends Handler with UxTesting {
  self: ScalatraKernel =>

  private val UxTestingCookieNamePrefix = "UxTesting_"
  private val UxTestingDefaultValue = UxTestingCookieNamePrefix + "NONE"

  override def currentVersionFor(testName:String) = {
	  val cookies = request.getCookies.filter(_.getName.startsWith(UxTestingCookieNamePrefix))
	  val currentCookie = if(cookies.size == 0) writeCookie(UxTestingCookieNamePrefix + testName, getNextValueFor(testName)) else cookies(0)
    currentCookie.getValue
  }

  override def checkAndSetUniqueEvent(testName:String, versionName:String, eventName:String, value:String) = {
    val cookieName = UxTestingCookieNamePrefix + testName + "_" + versionName + "_" + eventName
    val cookie = request.getCookies.find(_.getName.startsWith(cookieName))

    if(!cookie.isDefined) {
      var newValue = if(value.length == 0) UxTestingDefaultValue else value
      writeCookie(cookieName, newValue)
      false
    } else if(cookie.get.getValue != UxTestingDefaultValue && cookie.get.getValue != value) {
      writeCookie(cookieName, value)
      false
    } else {
      true
    }
  }

  private def writeCookie(name:String, value:String) = {
    val cookie = new Cookie(name, value)
    cookie.setPath("/")
    // cookie.setMaxAge(2)
    response.addCookie(cookie)
    cookie
  }
}
