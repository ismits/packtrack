package org.packtrack.utils.purolator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ShipmentWeight {
	private Logger logger

    private String estimated
    private String unit

   	public String toString() {
		String out = ""
		out += "Weight:" + "\n"
		out += "-> Estimated:             $estimated" + "\n"
		out += "-> Unit:                  $unit" + "\n"
		return out
	}
}
