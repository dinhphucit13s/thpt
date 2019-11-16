package fpt.dps.dtms.service.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {
	
	private List<String> workflowItems;
	
	public List<String> getWorkflowItems() {
		return workflowItems;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("userTask")) {
			// create a new Employee and put it in Map
			String id = attributes.getValue("id");
			// initialize list
			if (workflowItems == null)
				workflowItems = new ArrayList<>();
			workflowItems.add(id);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
	}
}
