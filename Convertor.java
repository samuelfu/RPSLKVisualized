/**
 * Convertor converts String choices to int choices
 * 
 * @author Samuel Fu ssf2130
 *
 */
public class Convertor {
	private final String[] POSSIBLE_CHOICE_STRING = { "Rock", "Paper", "Scissors", "Spock", "Lizard" };

	/**
	 * @param playerChoiceString
	 *            player choice in String form
	 * @return player choice in integer form
	 */
	public int convertChoiceToInt(String playerChoiceString) {
		 if (playerChoiceString.equals(POSSIBLE_CHOICE_STRING[0]))
			return 0;
		else if (playerChoiceString.equals(POSSIBLE_CHOICE_STRING[1]))
			return 1;
		else if (playerChoiceString.equals(POSSIBLE_CHOICE_STRING[2]))
			return 2;
		else if (playerChoiceString.equals(POSSIBLE_CHOICE_STRING[3]))
			return 3;
		else
			return 4;
	}

}
