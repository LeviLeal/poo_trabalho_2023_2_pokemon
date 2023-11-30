package poo_trabalho_pokemon;
import java.io.File;
import java.util.*;
class Save {
	private File stream;
	private boolean newGame;
	Save(){
		this.newGame = true;
		try {
			stream = new File("./config/");
			stream.mkdir();
			stream = new File("./config/config.txt");
			stream.createNewFile();
		} catch (Exception e) {
			System.out.println("Erro ao criar diretorio ou arquivo" + e.getMessage());
		}
	}
	
	public void saveConfig () {
		
	}
	
	public String readConfig () {
		return "";
	}
	
	public boolean isNewGame() {
		return newGame;
	}
}

enum Type {
	ELETRIC, GRASS, WATER, FIRE, FIGHTING, GHOST, NORMAL, PSYCHIC, SPECIAL;
	public String getWeakness() {
		String nome = name();
		String weakness = "None";
		switch(nome) {
			case "FIRE":
				weakness = "WATER";
				break;
			case "WATER":
				weakness = "GRASS";
				break;
			case "ELETRIC":
				weakness = "GRASS";
				break;
			case "GRASS":
				weakness = "FIRE";
				break;
			case "FIGHTING":
				weakness = "PSYCHIC";
				break;
			case "GHOST":
				weakness = "NORMAL";
			case "NORMAL":
				weakness = "GHOST";
				break;
			case "PSYCHIC":
				weakness = "GHOST";
		}
		return weakness;
	}
}

class Attack {
	private Type type;
	private String name;
	private int damage;
	Attack(String name, Type type, int damage){
		this.type = type;
		this.name = name;
		this.damage = damage;
	}
}

interface Item {
	public void use();
}

class Consumable implements Item {
	private String itemName;
	private Pokemon pokemon;
	Consumable (String itemName, Pokemon pokemon) {
		this.itemName = itemName;
		this.pokemon = pokemon;
	}
	public void use() {
		switch(itemName) {
			case "Potion":
				Potion();
				break;
			case "Protein":
				Protein();
				break;
			case "Candy":
				Candy();
				break;
		}
	}
	public void Potion () {
		pokemon.setHP(0);
	}
	public void Protein() {
		pokemon.setAtkFactor(pokemon.getAtkFactor() + 1);
	}
	public void Candy() {
		pokemon.levelUP();
	}
}

class PokeBall implements Item {
	public void use(Pokemon target) {
		target.setHP(target.getHP() / 2 - 1);
		if(target.getHP() < target.getMaxHP() / 2) {
			catchTarget(target);
		} else {
			Main.msg("A Pokebola falhou! Parece que ainda esta disposto a lutar...");
		}
	}
	public void catchTarget(Pokemon target) {
		Random random = new Random();
        double chance = random.nextDouble();
		if (chance < 0.5) {
			Main.msg("A Pokebola falhou! Parece que ainda esta disposto a lutar...");
		} else {
			Main.msg("Parabéns, você capturou um " + target.getName());
		}
	}
}

class Pokemon {
	private String name;
	private String type;
	private int level;
	private int hp;
	private int maxHP;
	private int atkFactor;
	private ArrayList<Attack> attacks;
	
	Pokemon(String name, String type, int level, int hp, Attack atk1, Attack atk2, Attack atk3, Attack atk4) {
		this.name = name;
		this.type = type;
		this.level = level;
		this.hp = hp;
		this.maxHP = hp;
		this.atkFactor = 0;
		this.attacks = new ArrayList<>();
		this.attacks.add(atk1);
		this.attacks.add(atk2);
		this.attacks.add(atk3);
		this.attacks.add(atk4);
	}
	
	public Attack () {
		return
	}

	public void setHP(int hp) {
		this.hp = hp > maxHP ? maxHP : hp;  
	}
	public int getHP() {
		return this.hp;
	}
	public int getMaxHP() {
		return this.maxHP;
	}
	public int getAtkFactor() {
		return this.atkFactor;
	}
	public void setAtkFactor(int atkFactor) {
		this.atkFactor = atkFactor;
	}
	public void levelUP() {
		this.atkFactor += 1;
		maxHP += 1;
	}
	public String getName() {
		return this.name;
	}
}

class Battle {
	Pokemon myPokemon;
	Pokemon opponent;
	String winner;
	String loser;
	Battle(Pokemon myPokemon, Pokemon opponent){
		this.myPokemon = myPokemon;
		this.opponent = opponent;
		this.winner = "undefined";
	}
	public void nextRound(){
		if (!winner.equals("undefined"))
			endBattle();
		else{
			Main.msg(myPokemon.getName() " usou ");
			Main.msg(opponent.getName() " sofreu " + "de dano")
			if(opponent.getHP() <= 0)
				endBattle();
			Main.msg(opponent.getName() " usou ");
			Main.msg(myPokemon.getName() " sofreu " + "de dano")
			if(myPokemon.getHP() <= 0)
				endBattle();
		}
	}

	private void endBattle(){
		Main.msg(getLoser() + " foi derrotado, " + getWinner() + " é o vencedor!");
	}

	private void getLoser(){return loser};
	private void getWinner(){return winner};
}

class Player {
	private String nick;
	private String sex;
	int insigneas;
	ArrayList<Pokemon> myPokemons = new ArrayList<Pokemon>();
	
	Player (String nick, String sex, int insigneas) {
		this.nick = nick;
		this.sex = sex;
		this.insigneas = insigneas;
	}
	
	public String getNick() {
		return this.nick;
	}
	
	public int getInsigneas() {
		return this.insigneas;
	}
	
	public String toString() {
		return "================\n"
				+ "     Nome:  " + getNick() 
				+ "Insígnias: " + getInsigneas();
	}
}

// se ja tiver tudo feito, então pega os valores do config na main

class Game {
	private ArrayList <Pokemon> pokemons;
	private ArrayList <Item> items;
	private Player player;
	private Boolean status;
	Game (String playerSex, String playerNick, int insigneas) {
		this.player = new Player(playerNick, playerSex, insigneas);
		this.pokemons = new ArrayList<>();
		this.items = new ArrayList<>();
		
		this.pokemons.add(new Pokemon("Charmander", "FOGO", 39, 1, new Attack("Scratch", Type.NORMAL, 40), 
				new Attack("Ember", Type.FIRE, 40), new Attack("Slash", Type.NORMAL, 70), new Attack("Fire Spin", Type.FIRE, 35)));
		this.pokemons.add(new Pokemon("Bulbassauro", "GRAMA", 45, 1, new Attack("Trackle", Type.NORMAL, 40), 
				new Attack("Vine Whip", Type.GRASS, 45), new Attack("Razor Leaf", Type.GRASS, 55), new Attack("Take Down", Type.NORMAL, 90)));
		this.pokemons.add(new Pokemon("Squirtle", "AGUA", 45, 1, new Attack("Tackle", Type.NORMAL, 40), 
				new Attack("Water Gun", Type.WATER, 40), new Attack("Rapid Spin", Type.NORMAL, 50), new Attack("Water Pulse", Type.WATER, 60)));
		this.pokemons.add(new Pokemon("Pikachu", "ELETRICO", 35, 1, new Attack("Quick Attacak", Type.NORMAL, 40), 
				new Attack("Thunder Shock", Type.ELETRIC, 40), new Attack("Discharge", Type.ELETRIC, 80), new Attack("Iron Tail", Type.NORMAL, 100)));

	}
	public String getPlayerNick() {
		return this.player.getNick();
	}
	public void menu () {
		Main.msg("===================\n"
				+ "| Avancar | MyInfo | Quit");
	}
	public void battleMenu () {
		Main.msg("=====================\n"
				+ "| Lutar | Fugir... |"
				+ "\n====================="
				+ "\n O que vai escolher?");
	}
	
	public void shopMenu () {
		Main.msg("==================\n"
		+ "|Loja|\n" 
		+ "Pokebola | 50C");
	}

	public void addItem (String itemType) {
		Item newItem;
		switch(itemType) {
			case "Pokebola":
				newItem = new PokeBall();
		}
		
	}

	public void removeItem(int index){
		this.items.remove(index);
	}

	public ArrayList<Item> getItems(){
		return this.items;
	}

	public boolean getStatus () { return this.status; }
	public void setStatus (String status) { this.status = status; }
}

public class Main {
	public static void main (String[]args) {
		Save save = new Save();	
		Game game = null;
		
		// DEBUG!
		// if(true) {
		// 	PokeBall pkb = new PokeBall(new Pokemon("Charmander", "FOGO", 39, 1, new Attack("Scratch", Type.NORMAL, 40), 
		// 		new Attack("Ember", Type.FIRE, 40), new Attack("Slash", Type.NORMAL, 70), new Attack("Fire Spin", Type.FIRE, 35)));
		// 	pkb.use();
		// 	return;
		// }
		
		if(save.isNewGame()) {
			msg("Bem vindo ao mundo de pokemon!\nEsse mundo é hábitado por\n "
					+ "criaturas chamadas Pokemon. Para algumas pessoas pokemons são pets. Para"
					+ "outras, são para batalhas. \nMas primeiro, conte-me qual seu nome");
			String firstAnswer = nextLine();
			msg("Escolha seu Pokemon. Ha quatro opcoes! Digite:\nC para Charmander\nS para Squirtle\nB para Bulbassauro\nP para Pikachu!");
			String secondAnswer = nextLine();
			game = new Game(firstAnswer, secondAnswer, 0);
			msg("\n" + game.getPlayerNick() + " foi teleportado para sua cidade, é hora de começar sua jornada!");
		} else 
			msg("Bem-vindo de volta!");
		while (true) {

			switch (game.getStatus()) {
				case "battle"
					game.battleMenu();
					break;
				case "shop":
					game.shopMenu();
					break;
				default:
					game.menu();
			}


			String nextLine = nextLine();
			msg("> | ");
			String [] splitedLine = nextLine.split("")
			// Coloquei o primeiro em uma variavel pois eh muito usado
			String firstArg = splitedLine[0];
			
			if(!game.inBattle()) {

			}

			switch (game.getStatus()) {
				case "battle"
					switch(){
						case " ":
						default:
							msg("Opção escolhida não existe");
					}
					break;
				case "shop":
					game.shopMenu();
					break;
				default:
					game.menu();
			}

			// game.menu();
			// String choose = nextLine();
			// game.playerChoose(choose);
		}
	}
	private static Scanner scanner = new Scanner(System.in);
	public static void msg(String message) { System.out.println(message); }
	private static String nextLine() { return scanner.nextLine(); }
}