package com.marshall.servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseManager {

	private Connection connection;
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "concentration_project";
	String driver = "com.mysql.jdbc.Driver";
	String userName = "admin";
	String password = "admin";

	public DatabaseManager() {
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url + dbName, userName,
					password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Player setResultToDatabase(Player player) {
		
		try {
			PreparedStatement preparedStatement;
			String setPlayerData = "INSERT INTO players (Name, Points) VALUES (?, ?)";
			
			preparedStatement = connection.prepareStatement(setPlayerData);
			preparedStatement.setString(1, player.getName());
			preparedStatement.setInt(2, player.getPoints());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet resultSet = statement.executeQuery("SELECT Id FROM Players ORDER BY Id DESC LIMIT 1;");
			resultSet.next();
			int userId = resultSet.getInt(1);
			player.setId(userId);
			
			String setAnswersData = "INSERT INTO answers (UserId, QuestionId, IsCorrect, Time) VALUES (?, ?, ?, ?)";
			for (int i = 0; i < player.getQuestionsList().size() && i < player.getTimesList().size(); i++) {
				preparedStatement = connection.prepareStatement(setAnswersData);
				preparedStatement.setInt(1, userId);
				preparedStatement.setInt(2, (i+1));
				preparedStatement.setBoolean(3, player.getQuestionsList().get(i));
				preparedStatement.setDouble(4, player.getTimesList().get(i));
				
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
			
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return player;
	}
	
	public ArrayList<Player> getTheBestPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		
		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet resultSet = statement.executeQuery("SELECT Id, Name, Points, " +
					"(SELECT SUM(a.Time) FROM answers a WHERE a.UserId = p.Id) AS Time " +
					"From players p ORDER BY Points DESC, Time ASC LIMIT 20");
			
			while (resultSet.next()) {
				Player player = new Player();
				player.setId(resultSet.getInt(1));
				player.setName(resultSet.getString(2));
				player.setPoints(resultSet.getInt(3));
				player.setTime(resultSet.getDouble(4));
				
				players.add(player);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return players;
	}

}
