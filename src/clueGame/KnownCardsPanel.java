package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class KnownCardsPanel extends JPanel{
	private JPanel peoplePanel;
	private JPanel roomsPanel;
	private JPanel weaponsPanel;
	
	//Sets up main panel with its three sub panels for each subset of card types
	public KnownCardsPanel() {
		this.setLayout(new GridLayout(3, 1));
		this.setBorder(new TitledBorder (new EtchedBorder(), "Known Cards"));
		
		makePeoplePanel();
		makeRoomsPanel();
		makeWeaponsPanel();

	}

	private void makePeoplePanel() {
		//Set up people panel
		peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout(0, 1));
		peoplePanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		this.add(peoplePanel);	

		//Label for hand
		JLabel handLabel = new JLabel("In Hand:");
		peoplePanel.add(handLabel);

		//Add "none" field to hand panel
		JTextField noneHand = new JTextField("None");
		noneHand.setEditable(false);
		peoplePanel.add(noneHand);

		//Label for seen
		JLabel seenLabel = new JLabel("Seen:");
		peoplePanel.add(seenLabel);

		//Add "none" field to seen panel
		JTextField noneSeen = new JTextField("None");
		noneSeen.setEditable(false);
		peoplePanel.add(noneSeen);

	}

	private void makeRoomsPanel() {
		//Set up rooms panel
		roomsPanel = new JPanel();
		roomsPanel.setLayout(new GridLayout(0, 1));
		roomsPanel.setPreferredSize(new Dimension(200, 800));
		roomsPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		this.add(roomsPanel);
		
		//Label for hand
		JLabel handLabel = new JLabel("In Hand:");
		roomsPanel.add(handLabel);

		//Add "none" field to hand panel
		JTextField noneHand = new JTextField("None");
		noneHand.setEditable(false);
		roomsPanel.add(noneHand);

		//Label for seen
		JLabel seenLabel = new JLabel("Seen:");
		roomsPanel.add(seenLabel);

		//Add "none" field to seen panel
		JTextField noneSeen = new JTextField("None");
		noneSeen.setEditable(false);
		roomsPanel.add(noneSeen);


	}

	private void makeWeaponsPanel() {
		//Set up weapons panel
		weaponsPanel = new JPanel();
		weaponsPanel.setLayout(new GridLayout(0, 1));
		weaponsPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		this.add(weaponsPanel);

		//Label for hand
		JLabel handLabel = new JLabel("In Hand:");
		weaponsPanel.add(handLabel);

		//Add "none" field to hand panel
		JTextField noneHand = new JTextField("None");
		noneHand.setEditable(false);
		weaponsPanel.add(noneHand);

		//Label for seen
		JLabel seenLabel = new JLabel("Seen:");
		weaponsPanel.add(seenLabel);

		//Add "none" field to seen panel
		JTextField noneSeen = new JTextField("None");
		noneSeen.setEditable(false);
		weaponsPanel.add(noneSeen);
	}

	

	public void updatePanel(ArrayList<Card> handCards, ArrayList<Card> seenCards){
		//Remove all hand and seen cards
		peoplePanel.removeAll();
		weaponsPanel.removeAll();
		roomsPanel.removeAll();
		
		//Adds in hand label to each panel
		JLabel handroomsLabel = new JLabel("In Hand:");
		roomsPanel.add(handroomsLabel);
		JLabel handpeopleLabel = new JLabel("In Hand:");
		peoplePanel.add(handpeopleLabel);
		JLabel handweaponsLabel = new JLabel("In Hand:");
		weaponsPanel.add(handweaponsLabel);
		
		//Split the deck into different ArrayLists
		ArrayList<Card> playersHandLeft = new ArrayList<Card>();
		ArrayList<Card> weaponsHandLeft = new ArrayList<Card>();
		ArrayList<Card> roomsHandLeft = new ArrayList<Card>();
		for(int i = 0; i < handCards.size(); i++) {
			if(handCards.get(i).getType() == CardType.PERSON) {
				playersHandLeft.add(handCards.get(i));
			} else if(handCards.get(i).getType() == CardType.ROOM) {
				roomsHandLeft.add(handCards.get(i));
			} else if(handCards.get(i).getType() == CardType.WEAPON) {
				weaponsHandLeft.add(handCards.get(i));
			}
		}

		//Remove everything in players hand and updates
		if(!playersHandLeft.isEmpty()) {
			for(int i = 0; i < playersHandLeft.size(); i++) {
				JTextField playerCardName = new JTextField(playersHandLeft.get(i).getCardName());
				playerCardName.setEditable(false);
				peoplePanel.add(playerCardName);
			}
		} else {
			JTextField none = new JTextField("None");
			none.setEditable(false);
			peoplePanel.add(none);
		}
		
		//Remove everything in rooms hand and updates
		if(!roomsHandLeft.isEmpty()) {
			for(int i = 0; i < roomsHandLeft.size(); i++) {
				JTextField roomCardName = new JTextField(roomsHandLeft.get(i).getCardName());
				roomCardName.setEditable(false);
				roomsPanel.add(roomCardName);
			}
		} else {
			JTextField none = new JTextField("None");
			none.setEditable(false);
			roomsPanel.add(none);
		}
		
		//Remove everything in weapons hand and updates 
		if(!weaponsHandLeft.isEmpty()) {
			for(int i = 0; i < weaponsHandLeft.size(); i++) {
				JTextField weaponCardName = new JTextField(weaponsHandLeft.get(i).getCardName());
				weaponCardName.setEditable(false);
				weaponsPanel.add(weaponCardName);
			}
		} else {
			JTextField none = new JTextField("None");
			none.setEditable(false);
			weaponsPanel.add(none);
		}
		
		//Adds the label for seen for each sub panel
		JLabel seenweaponsLabel = new JLabel("Seen:");
		weaponsPanel.add(seenweaponsLabel);
		JLabel seenpeopleLabel = new JLabel("Seen:");
		peoplePanel.add(seenpeopleLabel);
		JLabel seenroomsLabel = new JLabel("Seen:");
		roomsPanel.add(seenroomsLabel);
		
		
		//Split the deck into different ArrayLists
		ArrayList<Card> playersLeft = new ArrayList<Card>();
		ArrayList<Card> weaponsLeft = new ArrayList<Card>();
		ArrayList<Card> roomsLeft = new ArrayList<Card>();
		for(int i = 0; i < seenCards.size(); i++) {
			if(seenCards.get(i).getType() == CardType.PERSON) {
				playersLeft.add(seenCards.get(i));
			} else if(seenCards.get(i).getType() == CardType.ROOM) {
				roomsLeft.add(seenCards.get(i));
			} else if(seenCards.get(i).getType() == CardType.WEAPON) {
				weaponsLeft.add(seenCards.get(i));
			}
		}

		//Remove everything in players seen and updates
		if(!playersLeft.isEmpty()) {
			for(int i = 0; i < playersLeft.size(); i++) {
				JTextField playerCardName = new JTextField(playersLeft.get(i).getCardName());
				playerCardName.setEditable(false);
				peoplePanel.add(playerCardName);
			}
		} else {
			JTextField none = new JTextField("None");
			none.setEditable(false);
			peoplePanel.add(none);
		}
		
		//Remove everything in rooms seen and updates
		if(!roomsLeft.isEmpty()) {
			for(int i = 0; i < roomsLeft.size(); i++) {
				JTextField roomCardName = new JTextField(roomsLeft.get(i).getCardName());
				roomCardName.setEditable(false);
				roomsPanel.add(roomCardName);
			}
		} else {
			JTextField none = new JTextField("None");
			none.setEditable(false);
			roomsPanel.add(none);
		}
		
		//Remove everything in weapons seen and updates
		if(!weaponsLeft.isEmpty()) {
			for(int i = 0; i < weaponsLeft.size(); i++) {
				JTextField weaponCardName = new JTextField(weaponsLeft.get(i).getCardName());
				weaponCardName.setEditable(false);
				weaponsPanel.add(weaponCardName);
			}
		} else {
			JTextField none = new JTextField("None");
			none.setEditable(false);
			weaponsPanel.add(none);
		}

	}

	public static void main(String [] args) {
		KnownCardsPanel panel = new KnownCardsPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame
		frame.setSize(250, 650);  // size the frame
		frame.setContentPane(panel); // put the panel in the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close

		//Add seen cards
		ArrayList<Card> seenCards = new ArrayList<Card>();
		seenCards.add(new Card("Miss Scarlett", CardType.PERSON));
		seenCards.add(new Card("Colonel Mustard", CardType.PERSON));
		seenCards.add(new Card("Knife", CardType.WEAPON));
		seenCards.add(new Card("Bathroom", CardType.ROOM));
		seenCards.add(new Card("Laboratory", CardType.ROOM));
		seenCards.add(new Card("Lecture Hall", CardType.ROOM));
		seenCards.add(new Card("Recreation Center", CardType.ROOM));
		seenCards.add(new Card("Student Center", CardType.ROOM));
		seenCards.add(new Card("Dormitory", CardType.ROOM));
		seenCards.add(new Card("Dining Room", CardType.ROOM));
		seenCards.add(new Card("Classroom", CardType.ROOM));
		seenCards.add(new Card("Visitor Center", CardType.ROOM));
		
		//Add hand cards
		ArrayList<Card> handCards = new ArrayList<Card>();
		handCards.add(new Card("Wrench", CardType.WEAPON));
		handCards.add(new Card("Colonel Mustard", CardType.PERSON));
		handCards.add(new Card("Rope", CardType.WEAPON));

		//Test update hand and seen cards
		panel.updatePanel(handCards,seenCards);

       
		frame.setVisible(true); // make it visible
		
		
	
		

	}
}
