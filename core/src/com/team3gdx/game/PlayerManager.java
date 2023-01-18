package com.team3gdx.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PlayerManager {

	HashMap<String,Integer> playerData;
	ArrayList<String> playerName;
	ArrayList<Integer> playerValues;
	
	public PlayerManager() {
		playerData = new HashMap<String,Integer> ();
		playerName = new ArrayList<String>();
		playerValues = new ArrayList<Integer>();
	}
	
	public player createPlayer(String name, Integer score) {
		player newPlayer = new player(name,score);
		return newPlayer;
	}
	
	public void savePlayer(player Player){
		playerData.put(Player.name,Player.score);
	}
	
	public void removePlyaer(String name) {
		playerData.remove(name);
	}
	
	public Integer getLengh() {
		return playerData.size();
	}
	
	public void cleanPlayerData(HashMap<String, Integer> playerData) {
		Set<String> PlayerData = playerData.keySet();
		Iterator<String> iterator = PlayerData.iterator();
		
		while(iterator.hasNext()) {
			String data = iterator.next();
			playerName.add(data);
			playerValues.add(playerData.get(data));
		}
	}
	
	public ArrayList<String> getName(){
		return playerName;
	}
	
	public ArrayList<Integer> getValues(){
		return playerValues;
	}
	
	public void reOrder(ArrayList<String> playerData, ArrayList<Integer> values) {
		ArrayList<String> newPlayerData = new ArrayList<>();
		ArrayList<Integer> newValues = new ArrayList<>();
		while (values.isEmpty() == false){
			int Max = Collections.max(values);
			int index = values.indexOf(Max);
			newPlayerData.add(playerData.get(index));
			newValues.add(Max);
			values.remove(index);
			playerData.remove(index);
		}
		playerName = newPlayerData;
		playerValues = newValues;
	}

	
}
