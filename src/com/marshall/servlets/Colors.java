package com.marshall.servlets;

public enum Colors {

	CZERWONY("red"), 
	ZIELONY("green"), 
	¯Ó£TY("yellow"), 
	SZARY("gray"), 
	TURKUSOWY("aqua"), 
	CZARNY("black"), 
	NIEBIESKI("blue"),
	FUKSJOWY("fuchsia"), 
	LIMONKOWY("lime"), 
	KASZTANOWY("maroon"), 
	GRANATOWY("navy"), 
	OLIWKOWY("olive"), 
	POMARAÑCZOWY("orange"),
	PURPUROWY("purple"), 
	SREBRNY("silver"), 
	BIA£Y("white"), 
	BR¥ZOWY("brown"), 
	KORALOWY("coral"), 
	Z£OTY("gold"), 
	LAWENDOWY("lavender"),
	RÓ¯OWY("pink");

	public String value;

	private Colors(String value) {
		this.value = value;
	}

}
