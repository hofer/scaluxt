package com.thoughtworks.scaluxt

import org.scalatra._
import javax.servlet.http.Cookie

trait UxTestingScalatra extends Handler with UxTesting {
  self: ScalatraKernel =>

  private val CookieNamePrefix = "UxTesting_"

  override def currentVersionFor(testName:String) = {
	val cookies = request.getCookies.filter(_.getName.startsWith(CookieNamePrefix))
	val currentCookie = 
	  if(cookies.size == 0) {
	    val cookie = new Cookie(CookieNamePrefix + testName, getNextValueFor(testName))
	    cookie.setPath("/")
	    // cookie.setMaxAge(2)
	    response.addCookie(cookie)
	    cookie
	  }
	  else cookies(0)
	
    currentCookie.getValue	
  }
}
