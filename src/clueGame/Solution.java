package clueGame;

public class Solution {
	public Card person;
	public Card room;
	public Card weapon;
	
	//Constructor
	public Solution(Card person, Card room, Card weapon) {
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	public void setPerson(Card person) {
		this.person = person;
	}

	public void setRoom(Card room) {
		this.room = room;
	}

	public void setWeapon(Card weapon) {
		this.weapon = weapon;
	}

	//Get person
	public Card getPerson() {
		return person;
	}

	//Get room
	public Card getRoom() {
		return room;
	}

	//Get weapon
	public Card getWeapon() {
		return weapon;
	}
	
	//Equals
	public boolean equals(Solution target) {
		return (this.person.equals(target.getPerson()) && this.room.equals(target.getRoom())
					&& this.weapon.equals(target.getWeapon()));
	}
	
}
