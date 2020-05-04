import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CSP {

	ArrayList<String> colors;
	HashMap<String, State> states;
	ArrayList<State> state_list;
	int iterations;
	int num_violations;
	int changes;
	
	CSP(ArrayList<String> colors, HashMap<String, State> states) {
		this.colors = colors;
		this.states = states;
		this.state_list = new ArrayList<State>();
		iterations = 0;
		num_violations = 48;
		changes = 0;
	}
	
	public void clearCSP() {
		for (int i = 0; i < state_list.size(); i++) {
			State temp = state_list.get(i);
			temp.clearValue();
			temp.clearPossibleColors();
			for (int j = 0; j < colors.size(); j++) {
				temp.addPossibleColor(colors.get(j));
			}
		}
	}
	
	// prints each state and its color, along with all of its neighbors
	// and their colors to easily identify any constraint violations
	public void printCSP() {
		for (int i = 0; i < state_list.size(); i++) {
			  System.out.println(state_list.get(i).getName() + "--> " + state_list.get(i).getColor());
			  for (int j = 0; j < state_list.get(i).getAdjacentStates().size(); j ++) {
				System.out.println("\t" + state_list.get(i).getAdjacentStates().get(j).getName() + "--> " + state_list.get(i).getAdjacentStates().get(j).getColor());
			  }
			}
	}
	
	public ArrayList<String> getColorList() {
		return colors;
	}
	
	public HashMap<String, State> getStateListString() {
		return states;
	}
	
	public void createStateList() {
		for (State s : states.values()) {
			state_list.add(s);
		}
	}
	
	public ArrayList<State> getStateList() {
		return this.state_list;
	}
	
	// returns true if every state is assigned a color
	// and if every state has a different color than its adjacent states
	public boolean isSolved() {
		for (int i = 0; i < state_list.size(); i++) {
			if (state_list.get(i).getColor() == null) {
				return false;
			}
		}
		for (int i = 0; i < state_list.size(); i++) {
			if (!state_list.get(i).isDifFromAdjState()) {
				return false;
			}
		}
		return true;
	}
	
	// returns an unassigned state with the highest number of adjacent states
	public State findMCV(int index) {
		int count  = 0;
		int max = -1;
		State MCV = state_list.get(0);
		
		for (int i = 0; i < state_list.size(); i++) {
			if (state_list.get(i).numAdjStates() >= max) {
				if (count >= index) {
					max = state_list.get(i).numAdjStates();
					MCV = state_list.get(i);
				}
			}
		}
		
		return MCV;
		
	}
	
	// returns an unassigned State with the fewest remaining possible colors
	public State findMRV() {
		
		int min = colors.size();
		State MRV = state_list.get(0);
		
		for (int i = 0; i < state_list.size(); i++) {
			if (state_list.get(i).getNumPossibleColors() <= min && state_list.get(i).getColor() == null) {
				MRV = state_list.get(i);
				min = state_list.get(i).getNumPossibleColors();
			}
		}
		
		return MRV;
		
	}
	
	// backtracking function that recursively calls itself
	// in order to preserve state of last valid assignment
	public boolean runBacktrack(State MCV) {
		
		// number of possible colors left for the given state
        int num_colors_left = MCV.getPossibleColors().size();
        
        // if there are none left then must terminate the search path
        if (num_colors_left == 0) {
            return false;
        }
        
        // used to identify and use colors form the list in a consistent order
        int index = 0;

        // might possibly try every single color
        while(index < num_colors_left) {
            
        	// assign new possible color to current state
            String new_color = MCV.getPossibleColors().get(index);
            MCV.changeValue(new_color);
            
            // keeping track of how many states we have searched
    		this.iterations++;

            // if we are still solving the problem
            if(!isSolved()) {
            	// prevent adjacent states from using that color
            	MCV.updateAdjStates(new_color);
            	// find next MRV and continue backtrack search
                State new_MRV = findMRV();
                if(runBacktrack(new_MRV)){
                    return true;
                } else {
                	// if the function returns false then we should step back
                	// and go to the last valid assignment
                	MCV.stepBack(new_color);
                	// will try the next color in this case
                    index++;
                }
            // if problem is solved
            } else {
            	return true;
            }

        }

        return false;
		
	}
	
	public int getNumViolations() {
		int count = 0;
		for (int i = 0; i < state_list.size(); i++) {
			if (!state_list.get(i).isDifFromAdjState()) {
				count++;
			}
		}
		return count;
	}
	
	// this function randomly selects state colors until there are less than 15
	// violations.  This actually happens suprsingly fast (about 1 second). After
	// this the function cycles through all violating states one at a time and 
	// assigns them a new color.  The function goes back to the old color value
	// if no progress is made, and tries other states whether they violate constraints
	// or not
	public int runLocalSearch() {
		
		long BEGIN = System.currentTimeMillis();
		
		Random rand = new Random();
		
		while (num_violations > 15) {
		
			for (int i = 0; i < state_list.size(); i++) {
				state_list.get(i).changeValue(colors.get(rand.nextInt(colors.size())));
			}
			
			num_violations = getNumViolations();
		
		}
		
		long END = System.currentTimeMillis();
		
		int new_violations = num_violations;
		int count = 0;
		
			while ((END-BEGIN < 1000*60) && (!isSolved())) {
				
				changes++;
				State change_this = getViolatingState(count % (state_list.size() + 5));
				String color = change_this.getColor();
				String color_new = change_this.getColor();
				
				if (change_this != null) {
					while (color == color_new) {
						int idx = rand.nextInt(colors.size());
						change_this.changeValue(colors.get(idx));
						color_new = colors.get(idx);
					}
				}
				
				new_violations = getNumViolations();
				
				if (new_violations < num_violations) {
					END = System.currentTimeMillis();
					continue;
				} else {
					// go back
					change_this.changeValue(color);
					count++;
					new_violations = getNumViolations();
					END = System.currentTimeMillis();
				}
				
			}
		
		return new_violations;
		
	}
	
	public State getViolatingState(int index) {
		if (index > 47) {
			return findMCV(index);
		}
		State back_up= state_list.get(0);
		int count = 0;
		for (int i = 0; i < state_list.size(); i++) {
			if (!state_list.get(i).isDifFromAdjState()) {
				if (count >= index) {
					return state_list.get(i);
				} else {
					count++;
				}
			}
		}
		return back_up;
		
	}
	
}
