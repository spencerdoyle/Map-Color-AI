import java.util.ArrayList;

public class State {

	String name;
	String color;
	int adj_state_count;
	ArrayList<State> adjacent_states;
	ArrayList<String> possible_colors;	// not used yet
	int possible_colors_count;
	
	State(String name, ArrayList<String> colors) {
		this.name = name;
		this.color = null;
		this.adj_state_count = 0;
		adjacent_states = new ArrayList<State>();
		possible_colors = new ArrayList<String>();
		for (int i = 0; i < colors.size(); i++) {
			possible_colors.add(colors.get(i));
		}
		possible_colors_count = possible_colors.size();
		
	}
	
	public void changeValue(String value) {
		this.color = value;
	}
	
	public void clearValue() {
		this.color = null;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public void addAdjacentState(State value) {
		this.adjacent_states.add(value);
		adj_state_count++;
	}
	
	public ArrayList<State> getAdjacentStates() {
		return this.adjacent_states;
	}
	
	public int numAdjStates() {
		return adj_state_count;
	}
	
	public boolean isDifFromAdjState() {
		for (int i = 0; i < adjacent_states.size(); i++) {
			if (adjacent_states.get(i).getColor().compareTo(this.color) == 0) {
				return false;
			}
		}
		return true;
	}
	
	public void clearPossibleColors() {
		this.possible_colors = new ArrayList<String>();
		this.possible_colors_count = 0;
	}
	
	public void removePossibleColor(String value) {
		possible_colors.remove(value);
		possible_colors_count--;
	}
	
	public void addPossibleColor(String value) {
		possible_colors.add(value);
		possible_colors_count++;
	}
	
	public int getNumPossibleColors() {
		return possible_colors_count;
	}
	
	public ArrayList<String> getPossibleColors() {
		return this.possible_colors;
	}
	
	public void updateAdjStates(String bad_color) {
		for (int i = 0; i < adjacent_states.size(); i++) {
			adjacent_states.get(i).removePossibleColor(bad_color);
		}
	}
	
	public void stepBack(String new_color) {
		this.color = null;
		this.possible_colors.add(new_color);
		for (int i = 0; i < adjacent_states.size(); i++) {
			adjacent_states.get(i).addPossibleColor(new_color);
		}
	}
	
}
