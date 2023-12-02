package poo_trabalho_pokemon;
import java.io.File;
import java.util.*;
class Save {
	private File stream;
	private boolean newGame;
	Save(){
		this.newGame = false;
		try {
			stream = new File("./config/");
			stream.mkdir();
			stream = new File("./config/config.txt");
			stream.createNewFile();
		} catch (Exception e) {
			System.out.println("Erro ao criar diretorio ou arquivo" + e.getMessage());
		}
	}
	
	public Game getGame () {

		String playerName = "Levi";
		String choosedPokemon = "C";	
		int insigneas = 0;

		return new Game(playerName, choosedPokemon, insigneas);
	}

	public void saveGame () {
		
	}
		
	public boolean isNewGame() {
		Game savedGame = getGame();
		if(savedGame != null)
			return false;
		else
			return true;
	}
}

enum Type {
	ELETRIC, GRASS, WATER, FIRE, NORMAL;
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
			case "NORMAL":
				weakness = "GHOST";
				break;
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
	public String getAttackName() {return this.name;}
	public int getDamage(){return this.damage;}
	public Type getType () {return this.type; }
}

interface IUsable {
	abstract public void use(Pokemon target);
}

class Item {	
	protected String itemName;
	protected Pokemon target;
	protected int price;
	protected void setTarget(Pokemon target) { this.target = target;	};
	protected Pokemon getTarget() { return this.target; } 
	public int getPrice() { return this.price; }
}

class Consumable extends Item implements IUsable {
	Consumable (String itemName) {
		this.itemName = itemName;
		this.target = target;
		this.price = 50;
	}
	public void use(Pokemon target) {
		this.target = target;
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
		target.setHP(0);
	}
	public void Protein() {
		target.setAtkFactor(target.getAtkFactor() + 1);
	}
	public void Candy() {
		target.levelUP();
	}
}
	
class PokeBall extends Item implements IUsable {
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
			Main.msg("Parabens, voce capturou um " + target.getName());
		}
	}
}

class Pokemon {
	private String name;
	private Type type;
	private int level;
	private int hp;
	private int maxHP;
	private int atkFactor;
	private ArrayList<Attack> attacks;
	
	Pokemon(String name, Type type, int level, int hp, Attack atk1, Attack atk2, Attack atk3, Attack atk4) {
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
	// define qual sera o ataque a usar
	public Attack getAttack(int index) { return this.attacks.get(index);}
	public void setHP(int hp) { this.hp = hp > maxHP ? maxHP : hp; }
	public int getHP() { return this.hp;}
	public int getMaxHP() { return this.maxHP;}
	public int getAtkFactor() { return this.atkFactor;}
	public void setAtkFactor(int atkFactor) {
		this.atkFactor = atkFactor;
	}
	public void levelUP() {
		this.atkFactor += 1;
		maxHP += 1;
	}

	public int getLevel () { return this.level;}
	public String getName() { return this.name; }
	public Type getType() { return this.type; }
	public String toString () {
		String strReturn = "Nome: " +  getName() + " | Tipo: " + getType() + " | Level: " + getLevel();
		return this.strReturn;
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
	public void nextRound(Attack myAttack, Attack opponentAttack){
		if (!getWinner().equals("undefined"))
			endBattle();
		else{
			Main.msg(myPokemon.getName() + " usou " + myAttack.getAttackName());
			damage(myAttack, opponent);
			Main.msg(opponent.getName() + " sofreu " + "de dano");
			if(opponent.getHP() <= 0)
				endBattle();
			Main.msg(opponent.getName() + " usou " + opponentAttack.getAttackName());
			damage(opponentAttack, myPokemon);
			Main.msg(myPokemon.getName() + " sofreu " + "de dano");
			if(myPokemon.getHP() <= 0)
				endBattle();
		}
	}

	private void endBattle(){
		Main.msg(getLoser() + " foi derrotado, " + getWinner() + " e o vencedor!");
	}

	private void damage(Attack attack, Pokemon target) {
		// Aqui que se define as vantagens
		int damage = attack.getDamage();
		int atkFactor = 30;
		String attackType = attack.getType().name();

		
		if (attackType.equals(target.getType().getWeakness()) )
			damage += (damage / 100) * (100 - atkFactor);
		else if (attack.getType().equals(target.getType()))
			damage -= (damage / 100) * (100 - atkFactor);

		target.setHP(target.getHP() - damage);
	}

	private String getLoser(){ return this.loser; }
	private String getWinner(){ return this.winner; }
}

class Player {
	private String nick;
	private String sex;
	private int money;
	int insigneas;
	private HashMap<String, Pokemon> myPokemons;
	private Pokemon currentPokemon;
	private ArrayList <Item> items;
	
	Player (String nick, String sex, int insigneas) {
		this.nick = nick;
		this.sex = sex;
		this.insigneas = insigneas;
		this.money = 0;
		this.items = new ArrayList<>();
		this.myPokemons = new HashMap<>();
	}

	public Pokemon getPokemon(String pokemonName) { return this.myPokemons.get(pokemonName); }
	public void addPokemon(Pokemon pokemon) { this.myPokemons.put(pokemon.getName(), pokemon);}
	public String getNick() { return this.nick; } 
	public int getInsigneas() { return this.insigneas; }
	public String toString() {
		return "================\n"
				+ "     Nome:  " + getNick() 
				+ "Insignias: " + getInsigneas();
	}
	private int getMoney() { return this.money; }
	public void withdraw(int money) { this.money -= money; }
	public void deposit (int money) { this.money += money; }
	public boolean canBuy (int purchase) {
		if (getMoney() - purchase < 0) {
			Main.msg("Voce nao possui dinheiro suficiente, jogue mais!");
			return false;
		} else
			return true;
	}
	public void removeItem(int index){ this.items.remove(index); }
	public ArrayList<Item> getItems(){ return this.items; }
	public void addItem(Item item) { this.items.add(item); }
	public void setCurrentPokemon (Pokemon pokemon) { this.currentPokemon = pokemon; }
	public Pokemon getCurrentPokemon () { return this.currentPokemon; }
	@Override
	public String toString () {
		String strReturn = "Nome: " + getPlayerNick() + " | Insigneas: " + getInsigneas() {
		
	};
		return;
	}
}

// se ja tiver tudo feito, entao pega os valores do config na main


class Game{
	private HashMap <String, Pokemon> pokemons;
	private Player player;
	private String status; 
	public Battle battle;
	Game (String playerNick, String choosedPokemon, int insigneas) {
		setStatus("idle");
		this.player = new Player(playerNick, choosedPokemon, insigneas);
		this.pokemons = new HashMap<>();
		
		this.pokemons.put("Charmander", new Pokemon("Charmander", Type.FIRE, 39, 1, new Attack("Scratch", Type.NORMAL, 40), 
				new Attack("Ember", Type.FIRE, 40), new Attack("Slash", Type.NORMAL, 70), new Attack("Fire Spin", Type.FIRE, 35)));
		this.pokemons.put("Bulbassauro", new Pokemon("Bulbassauro", Type.GRASS, 45, 1, new Attack("Trackle", Type.NORMAL, 40), 
				new Attack("Vine Whip", Type.GRASS, 45), new Attack("Razor Leaf", Type.GRASS, 55), new Attack("Take Down", Type.NORMAL, 90)));
		this.pokemons.put("Squirtle", new Pokemon("Squirtle", Type.WATER, 45, 1, new Attack("Tackle", Type.NORMAL, 40), 
				new Attack("Water Gun", Type.WATER, 40), new Attack("Rapid Spin", Type.NORMAL, 50), new Attack("Water Pulse", Type.WATER, 60)));
		this.pokemons.put("Pikachu", new Pokemon("Pikachu", Type.ELETRIC, 35, 1, new Attack("Quick Attacak", Type.NORMAL, 40), 
				new Attack("Thunder Shock", Type.ELETRIC, 40), new Attack("Discharge", Type.ELETRIC, 80), new Attack("Iron Tail", Type.NORMAL, 100)));

		Pokemon firstPokemon = null;
		switch(choosedPokemon) {
			case "C":
				firstPokemon = getPokemon("Charmander");
				break;
			case "S":
				firstPokemon = getPokemon("Squirtle");
				break;
			case "P":
				firstPokemon = getPokemon("Pikachu");
				break;
			case "B":
				firstPokemon = getPokemon("Bulbassauro");
				break;
			default:
				firstPokemon = getPokemon("Pikachu");
		}
		player.setCurrentPokemon(firstPokemon);
		player.addPokemon(firstPokemon);
	}
	public String getPlayerNick() { return this.player.getNick(); }
	public void menu () {
		Main.msg("===================\n"
				+ " (B) Batalhar | (M) MyInfo | (L) Loja |  (Q) Quit");
	}
	public void battleMenu () {
		Main.msg("=====================\n"
				+ "| (A) Atacar | (F) Fugir... |"
				+ "\n====================="
				+ "\n O que vai escolher?");
	}

	public void battle() {
		// Escolher um pokemon para enfrentar
		Pokemon opponent = null;
		
		int pokemonListSize = this.pokemons.size();
		Random randomizer = new Random();
			int randomNumber = randomizer.nextInt(pokemonListSize);
		
		int i = 0;
		for (Pokemon pokemon : this.pokemons.values()) {
			if (i == randomNumber) {
				opponent = pokemon;
				break;
			}
			i += 1;
		}
		this.battle = new Battle(player.getCurrentPokemon(), opponent);
		setStatus("Battle");
	}
	
	public void shopMenu () {
		Main.msg("==================\n"
		+ "|Loja|\n" 
		+ "Pokebola | 50C");
	}

	public void buyItem (String itemType) {
		Item newItem;
		
		switch(itemType) {
			case "Pokebola":
				newItem = new PokeBall();
			case "Potion":
				newItem = new Consumable("Potion");
				break;
			case "Protein":
				newItem = new Consumable("Protein");
				break;
			case "Candy":
				newItem = new Consumable("Candy");
				break;
			default:
				newItem = null;
				Main.msg("Esse item nao existe nao...");
				return;
		}
		
		if (player.canBuy(newItem.getPrice())) {
			player.withdraw(newItem.getPrice());
			Main.msg("Você comprou um " + itemType + "!");
			player.addItem(newItem);
		}
		
	}
	
	public Player getPlayer() { return this.player};
	public Pokemon getPokemon (String pokemonName) {return this.pokemons.get(pokemonName);}
	public String getStatus () { return this.status; }
	public void setStatus (String status) { this.status = status; }
}

public class Main {
	public static void main (String[]args) {
		Save save = new Save();	
		Game game = null;
					
		if(save.isNewGame()) {
			msg("Bem vindo ao mundo de pokemon!\nEsse mundo e habitado por\n "
					+ "criaturas chamadas Pokemon. Para algumas pessoas pokemons são pets. Para"
					+ "outras, sao para batalhas. \nMas primeiro, conte-me qual seu nome");
			String firstAnswer = nextLine();
			msg("Escolha seu Pokemon. Ha quatro opcoes! Digite:\nC para Charmander\nS para Squirtle\nB para Bulbassauro\nP para Pikachu!");
			String secondAnswer = nextLine();
			game = new Game(firstAnswer, secondAnswer, 0);
			msg("\n" + game.getPlayerNick() + " foi teleportado para sua cidade, é hora de começar sua jornada!");
		} else {
			msg("Bem-vindo de volta!");
			game = save.getGame();
		}
		// DEFINE QUAL SERA O MENU A MOSTAR BASEADO NO STATUS
		while (true) {
			Main.msg(game.getStatus());
			switch (game.getStatus()) {
				case "battle":
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
			String [] splitedLine = nextLine.split("");
			String firstArg = splitedLine[0];
			
			// ESCOLHA DE OPCOES
			switch (game.getStatus()) {
				case "battle":
					switch(firstArg){
						case " ":
						default:
							msg("Opcao escolhida nao existe");
					}
					break;
				case "shop":
					game.buyItem(firstArg);
					break;
				default:
					// MENU PADRAO
					switch (firstArg) {
						case "B":
							game.battle();
							break;
						case "M":
							game.player
							break;
						case "Q":
							Main.msg("Ate mais!");
							System.exit(0);
							break;
						case "L":
							game.setStatus("shop");
							break;
						default:
							msg("Opcao escolhida nao existe");
							break;
					}
			}
		}
	}
	private static Scanner scanner = new Scanner(System.in);
	public static void msg(String message) { System.out.println(message); }
	private static String nextLine() { return scanner.nextLine(); }
}