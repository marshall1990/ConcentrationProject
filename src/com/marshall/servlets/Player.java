package com.marshall.servlets;

import java.util.Hashtable;

public class Player {

	private int id;
	private String name;
	private int points;
	private Hashtable<Integer, Boolean> questionsList;
	private Hashtable<Integer, Double> timesList;
	private double time;
	private int scenarioId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Hashtable<Integer, Boolean> getQuestionsList() {
		return questionsList;
	}

	public void setQuestionsList(Hashtable<Integer, Boolean> questionsList) {
		this.questionsList = questionsList;
	}

	public Hashtable<Integer, Double> getTimesList() {
		return timesList;
	}

	public void setTimesList(Hashtable<Integer, Double> timesList) {
		this.timesList = timesList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public int getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}

}
