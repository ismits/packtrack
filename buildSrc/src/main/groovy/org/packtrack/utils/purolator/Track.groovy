package org.packtrack.utils.purolator

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.packtrack.utils.purolator.Shipment
import org.packtrack.utils.purolator.ShipmentHistoryEvent

import groovy.json.JsonSlurper
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

class Track {
	private Logger logger
	private String trackingId
	private Shipment shipment

	public Track(String trackingId) {
		logger = LoggerFactory.getLogger('org.packtrack.utils.purolator')
		this.trackingId = trackingId
		shipment = new Shipment()
	}

	public getStatus() {
		def url = "https://www.purolator.com"
		def postBody = [
				iwPreActions:'setup;validate;singleSearch;singleForward;forward;', 
				searchCompress:'', 
				search: trackingId, 
				buttonTrackSearch: 'Track']

		def http = new HTTPBuilder(url)
		http.client.setRedirectStrategy(new LaxRedirectStrategy())
		http.ignoreSSLIssues()

		http.request(POST, TEXT) {
			uri.path = "/en/ship-track/tracking-summary.page"
			uri.query = [submit:true, componentID:1359477580240]
			body = postBody
			requestContentType = URLENC

			response.success = { resp, reader ->
				def response = reader.text
				logger.debug("Full response is {}", response)
				parseHttpResponse(response)
			}

			response.failure = {
				logger.lifecycle("Failure response: {}", it.statusLine)
			}
		}
	}

	public parseHttpResponse(String response) {
		getHistoryFromResponse(response)
		getShipmentDetailsFromReponse(response)
		getPackageDetailsFromResponse(response)
	}

	public getHistoryFromResponse(String response) {
		def history = (response =~ /(?ms)var\sjsHistoryTable\s\=\s\[(.*?)\]\;/)
		if (history) {
			def raw = history[0][1]
			raw.split("\n").each { row ->
				if (row.trim().length() > 5) {
					def match = (row =~ /'(.*?)',\s*?'(.*?)',\s*?'(.*?)',\s*?'(.*?)'/)
					if (match) {
						ShipmentHistoryEvent event = new ShipmentHistoryEvent(match[0][1], match[0][2], match[0][3], match[0][4])
						shipment.history.events.add(event)
						logger.debug("Found Event: \n{}", event.toString())
					}
				}
			}
		}
	}

	public getShipmentDetailsFromReponse(String response) {
		def details = (response =~ /(?ms)var\sdetailsData\s\=.*?\{(.*)\}.*?var\sassocPackages/)
		if (details) {
			JsonSlurper jsonSlurper = new JsonSlurper()
			def json = jsonSlurper.setType(RELAX).parseText('{' + details[0][1] + "}")

			shipment.trackingNumber = json.trackingNumber
			shipment.status = json.status
			shipment.actualDelivery = json.actualDelivery
			shipment.product = json.product
			shipment.productDetails = json.productDetails
			shipment.createdDate = json.createdDate
			shipment.orgEstimatedDelivery = json.orgEstimatedDelivery
			shipment.deliveryContact = json.deliveryContact
			shipment.deliveryContactAddress = json.deliveryContactAddress
			shipment.signatureImage = json.signatureImage
			shipment.leadPin = json.leadPin
			shipment.leadStatus = json.leadStatus
			shipment.pickupLocationId = json.pickupLocationId
			shipment.piecePins = json.piecePins
			shipment.compressUnlock = json.compressUnlock
			shipment.latestScanType = json.latestScanType
			shipment.latestScanSubType = json.latestScanSubType
			shipment.trackingStatus = json.trackingStatus

			shipment.weight.estimated = json.weight.estimated
			shipment.weight.unit = shipment.weight.unit

			shipment.estimatedDelivery.serviceDateType = json.estimatedDelivery.serviceDateType
			shipment.estimatedDelivery.serviceDate = json.estimatedDelivery.serviceDate
			shipment.estimatedDelivery.deliverBy = json.estimatedDelivery.deliverBy
			shipment.estimatedDelivery.shipmentToCountry = json.estimatedDelivery.shipmentToCountry
			shipment.estimatedDelivery.shipmentStatus = json.estimatedDelivery.shipmentStatus
			shipment.estimatedDelivery.pieceStatus = json.estimatedDelivery.pieceStatus
			shipment.estimatedDelivery.actualDeliveryDate = json.estimatedDelivery.actualDeliveryDate
			shipment.estimatedDelivery.attentionScanCode = json.estimatedDelivery.attentionScanCode
			shipment.estimatedDelivery.productCode = json.estimatedDelivery.productCode
			shipment.estimatedDelivery.crossReferencePin = json.estimatedDelivery.crossReferencePin
			shipment.estimatedDelivery.scanDetail.attentionPattern = json.estimatedDelivery.scanDetail.attentionPattern

			shipment.origin.name = json.origin.name
			shipment.origin.address = json.origin.address
			shipment.origin.city = json.origin.city
			shipment.origin.province = json.origin.province
			shipment.origin.postalcode = json.origin.postalcode
			shipment.origin.country = json.origin.country

			shipment.destination.name = json.destination.name
			shipment.destination.address = json.destination.address
			shipment.destination.city = json.destination.city
			shipment.destination.province = json.destination.province
			shipment.destination.postalcode = json.destination.postalcode
			shipment.destination.country = json.destination.country
		}
	}

	public getPackageDetailsFromResponse(String response) {

	}	

	public printFullStatus() {
		logger.lifecycle("Shipment Details: \n{}", shipment.toString())
	}

	public printShortStatus() {
		logger.lifecycle("Shipment Quick Details:")
		logger.lifecycle("  Tracking Number: {}", shipment.trackingNumber)
		logger.lifecycle("  Status:          {}", shipment.status)
		logger.lifecycle("  Product:         {}", shipment.product)
		logger.lifecycle("  Deliver By:      {}", shipment.estimatedDelivery.deliverBy)
		logger.lifecycle("  Actual Delivery: {}", shipment.actualDelivery.replaceAll("T"," "))
	}
}