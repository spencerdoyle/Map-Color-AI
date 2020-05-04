import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		//ArrayList<String> input = new ArrayList<String>();
		
		int blank_line_count = 0;
		
		ArrayList<String> colors = new ArrayList<String>();
		HashMap<String, State> states = new HashMap<String,State>();
		
		String csp_input;
		
		while (blank_line_count == 0) {  	// read in colors
			
			csp_input = scanner.nextLine();
			
			if (csp_input.compareTo("") == 0) {
				//System.out.println("FOUND BREAK");
				blank_line_count++;
				continue;
			}
			
			colors.add(csp_input);
			
		}
		
		while (blank_line_count == 1) {		// read in states
			
			csp_input = scanner.nextLine();
			
			if (csp_input.compareTo("") == 0) {
				//System.out.println("FOUND BREAK");
				blank_line_count++;
				continue;
			}
			
			State state = new State(csp_input, colors);
			
			states.put(csp_input, state);
			
		}
		
		while (scanner.hasNextLine() && blank_line_count == 2) {		// read in edges
			
			csp_input = scanner.nextLine();
			
			if (csp_input.compareTo("1818") == 0) { // debug, remove for use in bash shell
				//System.out.println("INPUT END");
				blank_line_count++;
				break;
			}
			
			String[] edges = csp_input.split("\\s+");
			
			String state1 = edges[0];
			String state2 = edges[1];
			
			State stateOne = states.get(state1);
			State stateTwo = states.get(state2);
			
			stateOne.addAdjacentState(stateTwo);
			stateTwo.addAdjacentState(stateOne);
			
		}
		
		scanner.close();
		
		CSP csp = new CSP(colors, states);
		
		csp.createStateList();
		
		State mcv = csp.findMRV();
		
		csp.runBacktrack(mcv);

		csp.printCSP();
		
		System.out.println("Number of Nodes Searched: " + csp.iterations);
		
		csp.clearCSP();
		
		int violations = csp.runLocalSearch();
		
		csp.printCSP();
		
		System.out.println("Number of changes: " + csp.changes);
	}
	
	

}
