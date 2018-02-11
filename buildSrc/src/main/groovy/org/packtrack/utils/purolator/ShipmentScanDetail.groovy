package org.packtrack.utils.purolator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ShipmentScanDetail {
	private Logger logger

    private String attentionPattern

	public String toString() {
		String out = ""
		out += "Scan Detail:" + "\n"
		out += "-> Attention Pattern:     $attentionPattern" + "\n"
		return out
	}
}
