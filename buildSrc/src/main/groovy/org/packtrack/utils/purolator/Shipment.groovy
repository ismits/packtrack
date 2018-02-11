package org.packtrack.utils.purolator

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.packtrack.utils.purolator.ShipmentWeight
import org.packtrack.utils.purolator.ShipmentEstimatedDelivery
import org.packtrack.utils.purolator.ShipmentReferences
import org.packtrack.utils.purolator.ShipmentAddress
import org.packtrack.utils.purolator.ShipmentHistoryTable

class Shipment {
	private Logger logger
	private String trackingId

	private String trackingNumber
	private String status
	private String actualDelivery
	private String product
	private String productDetails
	private String createdDate
	private String orgEstimatedDelivery
	private String deliveryContact
	private String deliveryContactAddress
	private String signatureImage
	private String leadPin
	private String leadStatus
	private String pickupLocationId
	private String piecePins
	private String compressUnlock
	private String latestScanType
	private String latestScanSubType
	private String trackingStatus
	private ShipmentWeight weight = new ShipmentWeight()
	private ShipmentEstimatedDelivery estimatedDelivery = new ShipmentEstimatedDelivery()
	private ShipmentReferences references = new ShipmentReferences()
	private ShipmentAddress origin = new ShipmentAddress()
	private ShipmentAddress destination = new ShipmentAddress()

	private ShipmentHistoryTable history = new ShipmentHistoryTable()

	public String toString() {
		String out = ""
		out += "Tracking Number:          $trackingNumber" + "\n"
		out += "Status:                   $status" + "\n"
		out += "Actual Delivery:          $actualDelivery" + "\n"
		out += "Product:                  $product" + "\n"
		out += "Product Details:          $productDetails" + "\n"
		out += "Created Date:             $createdDate" + "\n"
		out += "Org Estimated Delivery:   $orgEstimatedDelivery" + "\n"
		out += "Delivery Contact:         $deliveryContact" + "\n"
		out += "Delivery Contact Address: $deliveryContactAddress" + "\n"
		out += "Signature Image:          $signatureImage" + "\n"
		out += "Lead Pin:                 $leadPin" + "\n"
		out += "Lead Status:              $leadStatus" + "\n"
		out += "Pickup Location Id:       $pickupLocationId" + "\n"
		out += "Piece Pins:               $piecePins" + "\n"
		out += "Compress Unlock:          $compressUnlock" + "\n"
		out += "Latest Scan Type:         $latestScanType" + "\n"
		out += "Latest Scan SubType:      $latestScanSubType" + "\n"
		out += "Tracking Status:          $trackingStatus" + "\n"
		out += "\n" + weight.toString()
		out += "\n" + estimatedDelivery.toString()
		out += "\nOrigin " + origin.toString()
		out += "\nDestination " + destination.toString()
		return out
	}
}
