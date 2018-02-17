package org.packtrack.utils.fedex

import org.slf4j.Logger
import org.slf4j.LoggerFactory
// import org.packtrack.utils.fedex.Shipment
// import org.packtrack.utils.fedex.ShipmentHistoryEvent

import groovy.json.JsonSlurper
import groovy.json.internal.LazyMap
import static groovy.json.JsonParserType.LAX as RELAX
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.XML
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.HTML
import org.apache.http.impl.client.LaxRedirectStrategy

class FedexTrack {
	private Logger logger
	private String trackingId
	private String jsonOutputDir
	// private Shipment shipment

	public FedexTrack(String trackingId, String jsonOutputDir) {
		logger = LoggerFactory.getLogger('org.packtrack.utils.fedex')
		this.trackingId = trackingId
		this.jsonOutputDir = jsonOutputDir
		// shipment = new Shipment()
	}

	public getStatus() {
		logger.lifecycle("\nTracking FedEx package: {}", trackingId)
		def url = "https://www.fedex.com"
		def postBody = [
				data: '{"TrackPackagesRequest":{"appType":"WTRK","appDeviceType":"DESKTOP","supportHTML":true,"supportCurrentLocation":true,"uniqueKey":"",' +
				  '"processingParameters":{},"trackingInfoList":[{"trackNumberInfo":{"trackingNumber":"' +
				  trackingId + '","trackingQualifier":"","trackingCarrier":""}}]}}', 
				action: 'trackpackages',
				locale: 'en_US',
				version: '1',
				format: 'json']

		def http = new HTTPBuilder(url)
		// http.client.setRedirectStrategy(new LaxRedirectStrategy())
		// http.ignoreSSLIssues()

		http.request(POST, JSON) {
			uri.path = "/trackingCal/track"
			headers.'X-Requested-With' = 'XMLHttpRequest'
			body = postBody
			requestContentType = URLENC

			response.success = { resp, json ->
				logger.debug("Full response is {}", json.toString())
				File jsonFile = new File(jsonOutputDir, trackingId + '.json')
				jsonFile.write(json.toString())
				parseHttpResponse(json)
			}

			response.failure = {
				logger.lifecycle("Failure response: {}", it.statusLine)
			}
		}
	}

	public parseHttpResponse(LazyMap response) {
		// getHistoryFromResponse(response)
		getShipmentDetailsFromReponse(response)
		// getPackageDetailsFromResponse(response)
	}

	public getHistoryFromResponse(LazyMap response) {
		// def history = (response =~ /(?ms)var\sjsHistoryTable\s\=\s\[(.*?)\]\;/)
		// if (history) {
		// 	def raw = history[0][1]
		// 	raw.split("\n").each { row ->
		// 		if (row.trim().length() > 5) {
		// 			def match = (row =~ /'(.*?)',\s*?'(.*?)',\s*?'(.*?)',\s*?'(.*?)'/)
		// 			if (match) {
		// 				ShipmentHistoryEvent event = new ShipmentHistoryEvent(match[0][1], match[0][2], match[0][3], match[0][4])
		// 				shipment.history.events.add(event)
		// 				logger.lifecycle("Found Event: \n{}", event.toString())
		// 			}
		// 		}
		// 	}
		// }
	}

	public getShipmentDetailsFromReponse(LazyMap response) {
		logger.lifecycle("Tracking successful: {}", response.TrackPackagesResponse.successful)
		logger.lifecycle("Status code size is: {}", response.TrackPackagesResponse.errorList.size())
		logger.lifecycle("Status code: {}", response.TrackPackagesResponse.errorList[0].code)
		logger.lifecycle("Status message: {}", response.TrackPackagesResponse.errorList[0].message)
		logger.lifecycle("Package count is: {}", response.TrackPackagesResponse.packageList.size())
		Integer itemNo = 0
		response.TrackPackagesResponse.packageList.each { item ->
			logger.lifecycle("Package {} of {}:", ++itemNo, response.TrackPackagesResponse.packageList.size())
			logger.lifecycle("  Found:         {}", !item.isNotFound)
			logger.lifecycle("  Pickup Date:   {}", item.pickupDt)
			logger.lifecycle("  Pickup Date:   {}", item.displayPickupDateTime)
			logger.lifecycle("  Tendered Date: {}", item.displayTenderedDateTime)
			logger.lifecycle("  Delivery Date: {}", item.displayActDeliveryDateTime)
			logger.lifecycle("  Service Code:  {}", item.serviceCD)
			logger.lifecycle("  Service Desc:  {}", item.serviceDesc)
		}
	}

	public getPackageDetailsFromResponse(String response) {

	}	

	public printFullStatus() {
		// logger.lifecycle("Shipment Details: \n{}", shipment.toString())
	}

	public printShortStatus() {
		// logger.lifecycle("Shipment Quick Details:")
		// logger.lifecycle("  Tracking Number: {}", shipment.trackingNumber)
		// logger.lifecycle("  Status:          {}", shipment.status)
		// logger.lifecycle("  Product:         {}", shipment.product)
		// logger.lifecycle("  Deliver By:      {}", shipment.estimatedDelivery.deliverBy)
		// logger.lifecycle("  Actual Delivery: {}", shipment.actualDelivery.replaceAll("T"," "))
	}
}