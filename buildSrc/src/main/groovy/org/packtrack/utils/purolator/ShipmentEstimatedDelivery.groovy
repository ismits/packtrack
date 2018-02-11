package org.packtrack.utils.purolator

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.packtrack.utils.purolator.ShipmentScanDetail

class ShipmentEstimatedDelivery {
	private Logger logger

	private String serviceDateType
	private String serviceDate
	private String deliverBy
	private String shipmentToCountry
	private String shipmentStatus
	private String pieceStatus
	private String actualDeliveryDate
	private String attentionScanCode
	private String productCode
	private String crossReferencePin
	private ShipmentScanDetail scanDetail = new ShipmentScanDetail()

	public String toString() {
		String out = ""
		out += "Estimated Delivery:" + "\n"
		out += "-> Service Date Type:     $serviceDateType" + "\n"
		out += "-> Service Date:          $serviceDate" + "\n"
		out += "-> Deliver By:            $deliverBy" + "\n"
		out += "-> Shipment To Country:   $shipmentToCountry" + "\n"
		out += "-> Shipment Status:       $shipmentStatus" + "\n"
		out += "-> Piece Status:          $pieceStatus" + "\n"
		out += "-> Actual Delivery Date:  $actualDeliveryDate" + "\n"
		out += "-> Attention Scan Code:   $attentionScanCode" + "\n"
		out += "-> Product Code:          $productCode" + "\n"
		out += "-> Cross Reference Pin:   $crossReferencePin" + "\n"
		out += scanDetail.toString()
		return out
	}
}
