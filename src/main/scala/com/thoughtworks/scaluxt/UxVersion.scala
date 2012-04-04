package com.thoughtworks.scaluxt

import com.yammer.metrics.Metrics
import com.yammer.metrics.core.Counter
import scala.collection.mutable.HashMap

object UxVersion {
	val A = "A"
	val B = "B"
}

case class UxVersion(val testName:String, val versionName:String, val percentage:Int) {
	def incVersionUsage = usageCount.inc
	def trackEvent(eventName:String) {
		var metric = eventMetrics.get(eventName)
		if(!metric.isDefined) {
			metric = Some(Metrics.newCounter(classOf[UxVersion], fullVersionName + "-" + eventName))
			eventMetrics.put(eventName, metric.get)
		}
		
		metric.get.inc
	}
	
	private val fullVersionName = testName + "-" + versionName
	private val usageCount = Metrics.newCounter(classOf[UxVersion], fullVersionName)
	private val eventMetrics = new HashMap[String, Counter]
}