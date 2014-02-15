package com.marshall.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/HelloServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int interaction = -1;
	private ArrayList<ScenarioObject> scenarioObjectList = null;
	private Player player = new Player();
	private Hashtable<Integer, Boolean> questionsList = new Hashtable<Integer, Boolean>();
	private Hashtable<Integer, Double> timesList = new Hashtable<Integer, Double>();
	private long start, stop;
	private int numberLastNode, points, sumPoints, testNumber;
	private ArrayList<Player> players;
	private boolean getHistoryData = true;
	private boolean isResultTable = true;
	private boolean isHistory = false;
	private ArrayList<Object> scenarios;
	private XMLParser xmlParser;
	
	public MainServlet() {
		super();
	}

	public void doPostAndGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		response.setCharacterEncoding("UTF-8");

		isResultTable = true;
		isHistory = false;
		
		if (interaction < 0) {
			xmlParser = new XMLParser(getServletContext());
			scenarios = xmlParser.findScenarios();
			interaction++;
		}

		if (Boolean.parseBoolean(request.getParameter("repeat"))) {
			interaction = 0;
			isResultTable = false;
			clearPoints();
		} else if (Boolean.parseBoolean(request.getParameter("back"))) {
			getHistoryData = false;
		} else if (Boolean.parseBoolean(request.getParameter("history"))) {
			isHistory = true;
			isResultTable = false;
		} else if (request.getParameter("username") != null) {
			player.setName(request.getParameter("username"));
			testNumber = Integer.parseInt(request.getParameter("test"));
			scenarioObjectList = (ArrayList<ScenarioObject>) scenarios.get(testNumber);
			numberLastNode = xmlParser.getNumbers().get(testNumber);
			player.setScenarioId(testNumber);
			clearPoints();
		} else if (Boolean.parseBoolean(request.getParameter("end"))) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			interaction = 0;
			isResultTable = false;
			clearPoints();
		} else if (request.getParameter("button") != null
				&& interaction <= numberLastNode) {
			stop = System.currentTimeMillis();
			boolean isCorrect = Boolean.parseBoolean(request
					.getParameter("button"));
			if (isCorrect)
				player.setPoints(player.getPoints() + points);
			questionsList.put(interaction, isCorrect);
			player.setQuestionsList(questionsList);
			timesList.put(interaction, ((stop - start) / 1000.0));
			player.setTimesList(timesList);
			interaction++;
		}

		PrintWriter writer = response.getWriter();

		writer.print("<!DOCTYPE html><html><head><meta charset=\"UTF-8\" /><title>Badanie koncentracji</title>");
		writer.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>");
		writer.print("<link rel=\"stylesheet\" href=\"" + request.getContextPath() + "/css/style.css" + "\" type=\"text/css\" />");
		writer.print("<link rel=\"stylesheet\" href=\"" + request.getContextPath() + "/css/colors.css" + "\" type=\"text/css\" />");
		writer.print("<link rel=\"stylesheet\" href=\"" + request.getContextPath() + "/css/backgrounds.css" + "\" type=\"text/css\" />");
		writer.println("<link rel=\"stylesheet\" media=\"(max-width: 640px)\" href=\"css/mobile.css\" type=\"text/css\" />");
		writer.print("</head><body>");
		writer.print("<div class=\"papers\">");

		if (scenarioObjectList.size() > 0 && interaction <= numberLastNode) {
			quizScreen(writer);
		}

		if (isResultTable) {
			resultScreen(writer);
		}
		
		if (isHistory) {
			historyScreen(writer);
		}

		writer.println("<h6 class=\"footer\">Politechnika Krakowska - Wydzia³ In¿ynierii Elektrycznej i Komputerowej :: 2014.</h6>");
		writer.print("</div>");
		writer.print("</body><html>");

		start = System.currentTimeMillis();

	}
	
	private void quizScreen(PrintWriter writer) {
		writer.print("<h1> Wybierz zgodny kolor tekstu z napisem</h1>");
		
		writer.print("<form class=\"quiz\" method=\"post\" action=\"MainServlet\">");

		for (int i = 0; i < scenarioObjectList.size(); i++) {
			boolean messageCondition = (i > 0) && scenarioObjectList.get(i).getNodeId() != scenarioObjectList.get(i-1).getNodeId();
			
			if (scenarioObjectList.get(i).getNodeId() == interaction) {
				if ((i == 0 || messageCondition) && !scenarioObjectList.get(i).getMessage().equals("")) {
					writer.println("<h2>" + scenarioObjectList.get(i).getMessage() + "</h2>");
				}
				
				if ((i == 0 || messageCondition)) {
					points = scenarioObjectList.get(i).getPoints();
					sumPoints += points;
				}

				boolean isCorrect = scenarioObjectList.get(i).getText()
						.equals(scenarioObjectList.get(i).getColor());
				String color, background;
				if (!scenarioObjectList.get(i).getColor().equals(""))
					color = Colors.valueOf(scenarioObjectList.get(i).getColor().toUpperCase()).value;
				else 
					color = "";

				if (!scenarioObjectList.get(i).getBackground().equals(""))
					background = Colors.valueOf(scenarioObjectList.get(i).getBackground().toUpperCase()).value;
				else
					background = "";

				writer.print("<button class=\"" + color + " bg_"
						+ background + "\" name=\"button\" value=\""
						+ isCorrect + "\" type=\"sumbit\">"
						+ scenarioObjectList.get(i).getText() + "</button>");

				isResultTable = false;
			}
		}

		writer.print("</form>");
		
		progressBar(writer);
	}
	
	private void resultScreen(PrintWriter writer) {
		writer.print("<h1> Gratulacje " + player.getName() + "</h1>");
		writer.print("<p class=\"center\"> Zdoby³eœ " + player.getPoints() + "/"
				+ sumPoints + " punktów! </p>");
		writer.println("<table>");
		writer.println("<tr>");
		writer.println("<th>Nr</th>");
		writer.println("<th>OdpowiedŸ</th>");
		writer.println("<th>Czas reakcji</th>");
		writer.println("<tr>");
		for (int i = 0; i < questionsList.size() && i < timesList.size(); i++) {
			writer.print("<tr>");
			writer.println("<td>" + (i+1) + "</td>");
			writer.println("<td>" + (questionsList.get(i) ? "Prawid³owa" : "Z³a") + "</td>");
			writer.println("<td>" + timesList.get(i) + " sek.</td>");
			writer.print("</tr>");
		}
		writer.println("</table>");

		writer.println("<form method=\"post\" action=\"MainServlet\">");
		writer.println("<button class=\"button\" name=\"repeat\" value=\"true\" type=\"submit\">Jeszcze raz</button>");
		writer.println("<button class=\"button\" name=\"history\" value=\"true\" type=\"submit\">Historia</button>");
		writer.println("<button class=\"button\" name=\"end\" value=\"true\" type=\"submit\">Koniec</button>");
		writer.println("</form>");
		
		if (getHistoryData) {
			DatabaseManager databaseManager = new DatabaseManager();
			player = databaseManager.setResultToDatabase(player);
		}
	}
	
	private void historyScreen(PrintWriter writer) {
		writer.println("<h1>Ranking najlepszych uczestników</h1>");
		writer.println("<table class=\"history\">");
		writer.println("<tr>");
		writer.println("<th>Miejsce</th>");
		writer.println("<th>Osoba</th>");
		writer.println("<th>Punkty</th>");
		writer.println("<th>Czas</th>");
		writer.println("</tr>");
		
		if (getHistoryData) {
			DatabaseManager databaseManager = new DatabaseManager();
			players = databaseManager.getTheBestPlayers(testNumber);
		}

		for (int i = 0; i < players.size(); i++) {
			if (player.getId() == players.get(i).getId())
				writer.println("<tr class=\"red\">");
			else
				writer.println("<tr>");
			writer.println("<td>" + (i+1) + "</td>");
			writer.println("<td>" + players.get(i).getName() + "</td>");
			writer.println("<td>" + players.get(i).getPoints() + "</td>");
			writer.println("<td>" + String.format("%.4f", players.get(i).getTime()) + " sek.</td>");
			writer.println("</tr>");
		}
		writer.println("</table>");
		writer.println("<form method=\"post\" action=\"MainServlet\" >");
		writer.println("<button class=\"button\" name=\"back\" value=\"true\" type=\"submit\">Powrót</button>");
		writer.println("<button class=\"button\" name=\"end\" value=\"true\" type=\"submit\">Koniec</button>");
		writer.println("</form>");
	}
	
	private void clearPoints() {
		player.setPoints(0);
		sumPoints = 0;
		getHistoryData = true;
	}

	private void progressBar(PrintWriter writer) {
		writer.println("<ul class=\"progressbar\">");
		for (int i = 0; i <= numberLastNode; i++) {
			writer.println("<li class=\"circle" + ((i <= interaction) ? "" : " bg_gray") + "\"><li>");
		}
		writer.println("</ul>");
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPostAndGet(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPostAndGet(request, response);
	}

}
