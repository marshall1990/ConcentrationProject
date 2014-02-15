package com.marshall.servlets;

import java.io.File;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

	private ArrayList<ScenarioObject> scenarioObjectList;
	private ArrayList<Object> scenarios;
	private ServletContext context;
	private int numberLastNode;
	private ArrayList<Integer> numbers;
	
	public XMLParser(ServletContext context) {
		this.context = context;
		scenarios = new ArrayList<Object>();
		numbers = new ArrayList<Integer>();
	}
	
	public ArrayList<Object> findScenarios() {
		File[] files = new File(context.getRealPath("\\scenarios\\")).listFiles();
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().equals("scenario"+(i+1)+".xml"))
				parserXML("scenario"+(i+1)+".xml");
		}
		
		return scenarios;
	}
	
	private void parserXML(String fileName) {
		try {
			File xmlFile = new File(context.getRealPath("\\scenarios\\" + fileName));
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);
			NodeList nodeList = document.getElementsByTagName("step");

			scenarioObjectList = new ArrayList<ScenarioObject>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				Element element = (Element) node;

				String message = element.getAttribute("message");
				int points = Integer.parseInt(element.getAttribute("points"));
				
				NodeList subNodeList = element.getElementsByTagName("button");

				for (int j = 0; j < subNodeList.getLength(); j++) {
					Element subElement = (Element) subNodeList.item(j);

					ScenarioObject object = new ScenarioObject();

					object.setNodeId(i);
					object.setMessage(message);
					object.setPoints(points);
					object.setText(subElement.getAttribute("text"));
					object.setColor(subElement.getAttribute("color"));
					object.setBackground(subElement.getAttribute("background"));

					scenarioObjectList.add(object);

					this.numberLastNode = i;
				}

			}
			
			scenarios.add(scenarioObjectList);
			numbers.add(this.numberLastNode);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Integer> getNumbers() {
		return this.numbers;
	}
}
