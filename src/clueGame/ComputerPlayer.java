package clueGame;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player{
	private ArrayList<Card> deck;
	public ComputerPlayer(String name, String color, int row, int col, Board board){
		super(name, color, row, col, board);
		deck = board.getDeck();
	}

	//create a solution
	public Solution createSuggestion() {
		//Split the deck into different ArrayLists
		ArrayList<Card> playersLeft = new ArrayList<Card>();
		ArrayList<Card> weaponsLeft = new ArrayList<Card>();
		ArrayList<Card> roomsLeft = new ArrayList<Card>();
		for(int i = 0; i < deck.size(); i++) {
			if(deck.get(i).getType() == CardType.PERSON) {
				playersLeft.add(deck.get(i));
			} else if(deck.get(i).getType() == CardType.ROOM) {
				roomsLeft.add(deck.get(i));
			} else if(deck.get(i).getType() == CardType.WEAPON) {
				weaponsLeft.add(deck.get(i));
			}
		}
		for(int i = 0; i < playersLeft.size(); i++) {
			boolean remove = false;
			for(int j = 0; j < seenCards.size(); j++) {
				if(seenCards.get(j).getCardName().equals(playersLeft.get(i).getCardName())) {
					remove = true;
				}
			}
			if(remove) {
				playersLeft.remove(i);
				i--;
			}
		}
		for(int i = 0; i < weaponsLeft.size(); i++) {
			boolean remove = false;
			for(int j = 0; j < seenCards.size(); j++) {
				if(seenCards.get(j).getCardName().equals(weaponsLeft.get(i).getCardName())) {
					remove = true;
				}
			}
			if(remove) {
				weaponsLeft.remove(i);
				i--;
			}
		}
		Card roomCard = null;
		for(int i = 0; i < roomsLeft.size(); i++) {
			if(this.room.getName() == roomsLeft.get(i).getCardName()) {
				roomCard = roomsLeft.get(i);
			}
		}
		Random r = new Random();
		Card player = playersLeft.get(r.nextInt(playersLeft.size()));
		Card weapon = weaponsLeft.get(r.nextInt(weaponsLeft.size()));

		return new Solution(player,roomCard,weapon);
	}

	//select targets
	public BoardCell selectTargets(int moveDistance, Board board) {
		board.calcTargets(board.getCell(row, col), moveDistance);
		Set<BoardCell> targets = board.getTargets(); //Get targets list
		
		if(targets.isEmpty()) {
			return null;
		}

		//Add to an ArrayList
		ArrayList<BoardCell> targetsList = new ArrayList<BoardCell>();
		for(BoardCell c: targets) {
			targetsList.add(c);
		}

		Random r = new Random();

		//If no rooms in list, select randomly
		boolean noRooms = true;
		for(int i = 0; i < targetsList.size(); i++) {
			if(targetsList.get(i).isRoom()) {
				noRooms = false;
			}
		}
		if(noRooms) {
			return targetsList.get(r.nextInt(targetsList.size())); //Random selection
		}
		
		//Loops through seen cards
		Map<Character, Room> map = board.getRoomMap();
		boolean inSeenList = false;
		for(int i = 0; i < seenCards.size(); i++) {
			if(seenCards.get(i).getType() != CardType.ROOM) {
				continue;
			}
			
			//Loop through targets list
			for(int j = 0; j < targetsList.size(); j++) {
				if(!targetsList.get(j).isRoom()) {
					continue;
				}

				Room room = map.get(targetsList.get(j).getInitial());
				if(seenCards.get(i).getCardName().equals(room.getName())) {
					inSeenList = true; //Set seen to true if it is in both lists
				}
			}
		}
		
		if(inSeenList) {
			return targetsList.get(r.nextInt(targetsList.size())); //If seen then return random
		}
		
		for(int i = 0; i < targetsList.size(); i++) {
			if(targetsList.get(i).isRoom()) {
				return targetsList.get(i); //If not seen, then return the room
			}
		}
		return targetsList.get(r.nextInt(targetsList.size()));
	}

}
