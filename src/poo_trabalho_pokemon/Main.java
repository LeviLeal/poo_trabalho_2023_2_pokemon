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
		Game game = new Game(playerName, choosedPokemon, insigneas);
		game.getPlayer().deposit(1000);
		return game;
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
	public boolean idleUse;
	protected String itemName;
	protected Pokemon target;
	protected int price;
	protected void setTarget(Pokemon target) { this.target = target;	};
	protected Pokemon getTarget() { return this.target; } 
	public int getPrice() { return this.price; }
	public String getName() { return this.itemName; }
	public boolean getIdleUse() { return this.idleUse; }
	public void setIdleUse(boolean idleUse) { this.idleUse = idleUse; }
	public String toString () {
		return "1x " + getName(); 
	}

	Item (String itemName) {
		this.itemName = itemName;
		this.idleUse = true;
	}
}

class Consumable extends Item implements IUsable {
	Consumable (String itemName) {
		super(itemName);
		this.target = target;
		this.price = 50;
	}
	public void use(Pokemon target) {
		this.target = target;
		switch(itemName) {
			case "Potion":
				Potion();
				setIdleUse(false);
				break;
			case "Protein":
				Protein();
				setIdleUse(true);
				break;
			case "Candy":
				setIdleUse(true);
				Candy();
				break;
		}
	}
	public void Potion () {
		target.setHP(target.getMaxHP());
		Main.msg("Seu pokemon foi curado pela pocao!");
	}
	public void Protein() {
		target.setAtkFactor(target.getAtkFactor() + 1);
		Main.msg("O ataque do seu pokemon aumentou!");
	}
	public void Candy() {
		target.levelUP();
		Main.msg(getName() + " subiu de nível!");
	}
}
	
class PokeBall extends Item implements IUsable {
	PokeBall (String itemName){
		super(itemName);
		setIdleUse(false);
	}
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
	public Attack getAttack() { 
		Random randomizer = new Random();
		return this.attacks.get(randomizer.nextInt(4));
	}
	public void setHP(int hp) { this.hp = hp > maxHP ? maxHP : hp; }
	public int getHP() { return this.hp;}
	public int getMaxHP() { return this.maxHP;}
	public void setAtkFactor(int atkFactor) { this.atkFactor = atkFactor; }
	public int getAtkFactor() {return this.atkFactor; }
	public void levelUP() {
		this.atkFactor += 1;
		maxHP += 1;
	}

	public int getLevel () { return this.level;}
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	public Type getType() { return this.type; }
	public String toString () {
		String strReturn = getName() + " | Tipo: " + getType() + " | Level: " + getLevel();
		return strReturn;
	}
}

class Battle {
	Pokemon myPokemon;
	Pokemon opponent;
	String winner;
	String loser;
	boolean battleStatus;

	Battle(Pokemon myPokemon, Pokemon opponent){
		battleStatus = true;
		this.myPokemon = myPokemon;
		this.opponent = opponent;
		this.winner = "undefined";
	}
	public void nextRound(){
		Attack myAttack = getMyPokemon().getAttack();
		Attack opponentAttack = getOpponent().getAttack();
		if (!getWinner().equals("undefined"))
			endBattle();
		else{

			// MEU ATAQUE
			Main.msg(myPokemon.getName() + " usou " + myAttack.getAttackName());
			int damage = damage(myAttack, getOpponent(), myPokemon.getAtkFactor());
			Main.msg(opponent.getName() + " selvagem sofreu " + damage + " de dano");
			getOpponent().setHP(getOpponent().getHP() - damage);
			if(opponent.getHP() <= 0) {
				setWinner(myPokemon.getName());
				setLoser(opponent.getName());
				Main.msg("Parabens!");
				endBattle();
				return;
			}
			// ATAQUE SELVAGEM
			Main.msg(opponent.getName() + " usou " + opponentAttack.getAttackName());
			damage = damage(opponentAttack, myPokemon, opponent.getAtkFactor());
			Main.msg(myPokemon.getName() + " sofreu " + damage + " de dano");
			getMyPokemon().setHP(getMyPokemon().getHP() - damage);
			if(myPokemon.getHP() <= 0) {
				setWinner(opponent.getName());
				setLoser(myPokemon.getName());
				Main.msg("Precisa treinar mais...");
				endBattle();
				return;
			}
			// OUTPUT DA VIDA
			Main.msg("\n" + myPokemon.getName() + " HP: " + myPokemon.getHP() + "|" + opponent.getName() + " HP: " + opponent.getHP());
		}
	}

	private void endBattle(){
		Main.msg(getLoser() + " foi derrotado, " + getWinner() + " e o vencedor!");
		this.battleStatus = false;
	}

	public boolean getBattleStatus() {return this.battleStatus;}

	private int damage(Attack attack, Pokemon target, int atkFactor) {
		// Aqui que se define as vantagens
		int damage = attack.getDamage() + atkFactor;
		int weakness = 30;
		String attackType = attack.getType().name();
		if (attackType.equals(target.getType().getWeakness()) )
			damage += (damage / 100) * (100 - weakness);
		else if (attack.getType().equals(target.getType()))
			damage -= (damage / 100) * (100 - weakness);		
		return damage;
	}

	private String getLoser(){ return this.loser; }
	private String getWinner(){ return this.winner; }
	private void setLoser(String pokemon){ this.loser = pokemon; }
	private void setWinner(String pokemon){  this.winner = pokemon; }
	public Pokemon getMyPokemon() { return this.myPokemon;}
	public Pokemon getOpponent() { return this.opponent;}
}

class Player {
	private String nick;
	private int money;
	int insigneas;
	private HashMap<String, Pokemon> myPokemons;
	private Pokemon currentPokemon;
	private ArrayList <Item> items;
	
	Player (String nick, String choosedPokemon, int insigneas) {
		this.nick = nick;
		this.insigneas = insigneas;
		this.money = 0;
		this.items = new ArrayList<>();
		this.myPokemons = new HashMap<>();
	}

	public Pokemon getPokemon(String pokemonName) { return this.myPokemons.get(pokemonName); }
	public void addPokemon(Pokemon pokemon) { this.myPokemons.put(pokemon.getName(), pokemon);}
	public String getNick() { return this.nick; } 
	public int getInsigneas() { return this.insigneas; }
	public int getMoney() { return this.money; }
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
	public Item getItem(int index){ return this.items.get(index); }
	public void addItem(Item item) { this.items.add(item); }
	public void setCurrentPokemon (Pokemon pokemon) { this.currentPokemon = pokemon; }
	public Pokemon getCurrentPokemon () { return this.currentPokemon; }
	@Override
	public String toString () {
		String strReturn = "================\n" + "Nome: " + getNick() + " | Insigneas: " + getInsigneas() + " | Money: " + getMoney() +"\nPokemons:";
		for (Pokemon pokemon : this.myPokemons.values()) {
			strReturn += pokemon.toString() + "\n";
		}
		strReturn += "Items:";
		for (Item item : this.items) {
			strReturn += item.toString() + "\n";
		}
		return strReturn;
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
		
		this.pokemons.put("Charmander", new Pokemon("Charmander", Type.FIRE, 1, 200, new Attack("Scratch", Type.NORMAL, 40), 
				new Attack("Ember", Type.FIRE, 40), new Attack("Slash", Type.NORMAL, 70), new Attack("Fire Spin", Type.FIRE, 35)));
		this.pokemons.put("Bulbassauro", new Pokemon("Bulbassauro", Type.GRASS, 1, 180, new Attack("Trackle", Type.NORMAL, 50), 
				new Attack("Vine Whip", Type.GRASS, 55), new Attack("Razor Leaf", Type.GRASS, 65), new Attack("Take Down", Type.NORMAL, 90)));
		this.pokemons.put("Squirtle", new Pokemon("Squirtle", Type.WATER, 1, 250, new Attack("Tackle", Type.NORMAL, 30), 
				new Attack("Water Gun", Type.WATER, 35), new Attack("Rapid Spin", Type.NORMAL, 40), new Attack("Water Pulse", Type.WATER, 50)));
		this.pokemons.put("Pikachu", new Pokemon("Pikachu", Type.ELETRIC, 1, 200, new Attack("Quick Attacak", Type.NORMAL, 40), 
				new Attack("Thunder Shock", Type.ELETRIC, 40), new Attack("Discharge", Type.ELETRIC, 80), new Attack("Iron Tail", Type.NORMAL, 100)));

		Pokemon firstPokemon = null;
		switch(choosedPokemon.toUpperCase()) {
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
				+ " (F) Fight | (M) MyInfo | (S) Shop | (B) Bag | (Q) Quit");
	}

	public void battleWinned () {
		Main.msg("Fim da batalha");
		player.getCurrentPokemon().levelUP();
		player.deposit(50);

		setStatus("idle");
		menu();
	}


	public void battleMenu () {

		Main.msg("=====================\n"
				+ "| (A) Atacar | (B) Bag | (F) Fugir... |"
				+ "\n====================="
				+ "\n O que vai escolher?");
	}

	public void startBattle() {
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

		Main.msg("Cuidado, um " + opponent.getName() + " selvagem apareceu!");
		opponent.setName(opponent.getName() + " selvagem");
		this.battle = new Battle(player.getCurrentPokemon(), opponent);
		setStatus("Battle");
	}

	public void fight() {
		Main.msg("Preste atencao, eh a proxima rodada!");
		getBattle().nextRound();
	}
	
	public void shopMenu () {
		Main.msg("==================\n"
		+ "|Loja| Seu dinheiro: " + player.getMoney() + "\n" 
		+ "(1) Pokebola | 50C\n"
		+ "(2) Potion   | 50C\n"
		+ "(3) Protein  | 70C\n"
		+ "(4) Candy    | 50C\n"
		+ "|(V)oltar|");
	}

	public void bagMenu () {
		int i = 1;
		String msg = "Items: \n";
		for (Item item : player.getItems()) {
			msg += i + ":" + item.toString();
			msg += "\n";
			i += 1;
		}

		msg += "\n (V) Voltar";

		Main.msg(msg);
	}

	public void useItem(int index) {
		Item item = player.getItem(index);

		if (item == null) {
			Main.msg("Esse item nao existe");
			return;
		}	

		if (!getBattle().getBattleStatus() && !item.getIdleUse()) {
			Main.msg("Esse item so serve durante batalhas");
			return;
		}
	
		if (item.getName().equals("Pokeball")) {
			PokeBall pokeball = (PokeBall) item;
			pokeball.use(getBattle().getOpponent());
			player.addPokemon(getBattle().getOpponent());
			setStatus("idle");
		} else {
			Consumable consumable = (Consumable) item;
			consumable.use(player.getCurrentPokemon());
		}
		player.removeItem(index);
	}

	public void buyItem (String itemType) {
		Item newItem;
		
		switch(itemType) {
			case "1":
				newItem = new PokeBall("Pokeball");
				break;
			case "2":
				newItem = new Consumable("Potion");
				break;
			case "3":
				newItem = new Consumable("Protein");
				break;
			case "4":
				newItem = new Consumable("Candy");
				break;
			case "V":
				setStatus("idle");
				return;
			default:
				newItem = null;
				Main.msg("Esse item nao existe nao...");
				return;
		}
		
		if (player.canBuy(newItem.getPrice())) {
			player.withdraw(newItem.getPrice());
			Main.msg("Voce comprou um " + newItem.getName() + "!");
			player.addItem(newItem);
		}
		
	}
	
	public Player getPlayer() { return this.player; }
	public void playerInfo () { Main.msg(getPlayer().toString()); }
	public Pokemon getPokemon (String pokemonName) {return this.pokemons.get(pokemonName);}
	public String getStatus () { return this.status; }
	public void setStatus (String status) { this.status = status; }
	public Battle getBattle () { return this.battle; }
}

public class Main {
	public static void main (String[]args) throws Exception {
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
			switch (game.getStatus()) {
				case "battle":
					if (game.getBattle().getBattleStatus() == false)
						game.battleWinned();
					else
						game.battleMenu();
					break;
				case "shop":
					game.shopMenu();
					break;
				case "bag": 
					game.bagMenu();
					break;
				default:
					game.menu();
			}
			String nextLine = nextLine();
			String [] splitedLine = nextLine.split("");
			String firstArg = splitedLine[0];
			firstArg = firstArg;
			firstArg = firstArg.toUpperCase();
			
			// ESCOLHA DE OPCOES
			switch (game.getStatus()) {
				case "battle":
					switch(firstArg){
						case "A":
							game.fight();
							break;
						case "B":
							game.setStatus("bag");
							break;
						case "F":
							msg("Voce eh um covarde...");
							game.setStatus("Idle");
							break;
						default:
							msg("Opcao escolhida nao existe!!");
					}
					break;
				case "shop":
					game.buyItem(firstArg);
					break;
				case "bag":
					switch(firstArg) {
						case "V":
							if (game.getBattle().getBattleStatus())
								game.setStatus("battle");
							else
								game.setStatus("idle");
							break;
						default:
							try {  
								Integer.parseInt(firstArg);  
								game.useItem(Integer.parseInt(firstArg));
							} catch(Exception e){  
								throw new Exception("Numeros, e nao letras, por favor");
							}  
					}
					break;
				default:
					// MENU PADRAO
					switch (firstArg) {
						case "F":
							game.startBattle();
							game.setStatus("battle");
							break;
						case "M":
							game.playerInfo();
							break;
						case "Q":
							Main.msg("Ate mais!");
							System.exit(0);
							break;
						case "S":
							game.setStatus("shop");
							break;
						case "B":
							game.setStatus("bag");
							break;
						default:
							msg("Opcao escolhida nao existe!");
							break;
					}
			}
		}
	}
	private static Scanner scanner = new Scanner(System.in);
	public static void msg(String message) { System.out.println(message); }
	private static String nextLine() { return scanner.nextLine(); }
}