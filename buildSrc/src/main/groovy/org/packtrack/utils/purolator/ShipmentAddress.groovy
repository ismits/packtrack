package org.packtrack.utils.purolator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ShipmentAddress {
	private Logger logger

	private String name
	private String address
	private String city
	private String province
	private String postalcode
	private String country

	public String toString() {
		String out = ""
		out += "Address:" + "\n"
        out += "-> Name:                  $name" + "\n"
        out += "-> Address:               $address" + "\n"
        out += "-> City:                  $city" + "\n"
        out += "-> Province:              $province" + "\n"
        out += "-> Postalcode:            $postalcode" + "\n"
        out += "-> Country:               $country" + "\n"
		return out
	}
}
