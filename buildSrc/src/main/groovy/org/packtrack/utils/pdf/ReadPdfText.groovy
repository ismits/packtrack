package org.packtrack.utils.pdf

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.PDFTextStripperByArea

class ReadPdfText {
	private Logger logger
	private String filename

	public ReadPdfText(String filename) {
		logger = LoggerFactory.getLogger('org.packtrack.utils.pdf')
		this.filename = filename
	}

	public String getText() {
		try {
			PDDocument document = PDDocument.load(new File(filename))
			if (!document.isEncrypted()) {
			    PDFTextStripper stripper = new PDFTextStripper()
			    String text = stripper.getText(document)
	    		return text
	    	}
		}
		catch (all) {
			logger.error("Error reading pdf")
			return null
		}
		finally {
			if (document) {
				document.close()
			}
		}
	}

	public List<String> getTrackingNumbers() {
		List<String> trackingNumbers = []
		String pdfText = getText()
		pdfText.eachLine { line -> 
			def matcher = (line =~ /\b(\d{12})\b/)
			if (matcher) {
				logger.debug(line)
				logger.debug("**************  Found tracking number: {}", matcher[0][0])
				if (!trackingNumbers.contains(matcher[0][1])) {
					trackingNumbers += matcher[0][1]
				}
			}
		}
		return trackingNumbers
	}
}