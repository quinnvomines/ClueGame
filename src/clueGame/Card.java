package clueGame;

public class Card{
	private String cardName;
	private CardType type;
	
	//Constructor
	public Card(String name, CardType type) {
		cardName = name;
		this.type = type;
	}
	
	//Get type
	public CardType getType() {
		return type;
	}

	//Get card name
	public String getCardName() {
		return cardName;
	}

	//Test if equals
	public boolean equals(Card target) {
		return (cardName == target.getCardName() && type == target.getType());
	}
}
