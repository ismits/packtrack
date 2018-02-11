package org.packtrack.utils.purolator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ShipmentHistoryEvent {
	private Logger logger

	private String date
	private String time
	private String locationId
	private String notes

	public ShipmentHistoryEvent(String date, String time, String locationId, String notes) {
		this.date = date
		this.time = time
		this.locationId = locationId
		this.notes = notes
	}

	public String toString() {
		String out = ""
		out += "Date:       $date\n"
		out += "Time:       $time\n"
		out += "Location:   $locationId\n"
		out += "Notes:      $notes\n"
		return out
	}
}
