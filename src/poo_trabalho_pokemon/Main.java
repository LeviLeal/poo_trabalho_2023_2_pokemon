package poo_trabalho_pokemon;
import java.io.File;
import java.util.*;

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

class SpecialAttack extends Attack {
	
}


class Save {
	File stream;
	boolean newGame;
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
}

class Attack {
	Type type;
	String name;
	int damage;
	Attack(String name, Type type, int damage){
		this.type = type;
		this.name = name;
		this.damage = damage;
	}
}

class Pokemon {
	String name;
	String type;
	int level;
	int hp;
	
	Pokemon(String name, String type, int level, int hp, Attack atk1, Attack atk2, Attack atk3, Attack atk4) {
		this.name = name;
		this.type = type;
		this.level = level;
		this.hp = hp;
	}
}

class Player {
	String nick;
	String sex;
	String pronome;
	int insigneas;
	ArrayList<Pokemon> myPokemons = new ArrayList<Pokemon>();
	
	Player (String nick, String sex, int insigneas) {
		this.nick = nick;
		this.sex = sex;
		this.insigneas = insigneas;
		this.pronome = sex.equals("menino") ? "o" : "a"; 
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
	ArrayList <Pokemon> pokemon;
	Player player;
	Boolean inBattle;
	Game (String playerSex, String playerNick, int insigneas) {
		this.player = new Player(playerNick, playerSex, insigneas);
		pokemon = new ArrayList<>();
		/*String name, String type, int level, int hp, Attack atk1, Attack atk2, Attack atk3, Attack atk4*/
		// String type, String name, int damage
		
		this.pokemon.add(new Pokemon("Charmander", "FOGO", 39, 1, new Attack("Scratch", Type.NORMAL, 40), 
				new Attack("Ember", Type.FIRE, 40), new Attack("Slash", Type.NORMAL, 70), new Attack("Fire Spin", Type.FIRE, 35)));
		this.pokemon.add(new Pokemon("Bulbassauro", "GRAMA", 45, 1, new Attack("Trackle", Type.NORMAL, 40), 
				new Attack("Vine Whip", Type.GRASS, 45), new Attack("Razor Leaf", Type.GRASS, 55), new Attack("Take Down", Type.NORMAL, 90)));
		this.pokemon.add(new Pokemon("Squirtle", "AGUA", 45, 1, new Attack("Tackle", Type.NORMAL, 40), 
				new Attack("Water Gun", Type.WATER, 40), new Attack("Rapid Spin", Type.NORMAL, 50), new Attack("Water Pulse", Type.WATER, 60)));
		this.pokemon.add(new Pokemon("Pikachu", "ELETRICO", 35, 1, new Attack("Quick Attacak", Type.NORMAL, 40), 
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
	public void playerChoose(String choose) {
		if (inBattle()) {
			switch(choose) {
				case "Fight":
					break;
				case "Fugir":
					break;
				default:
					Main.msg("Opção escolhida não existe");
			}
		} else {
			switch(choose) {
				case "Fight":
					break;
				case "Fugir":
					break;
				default:
					Main.msg("Opção escolhida não existe");
			}
		}
	}
	public boolean inBattle () { return this.inBattle; }
}

public class Main {
	public static void main (String[]args) {
		Boolean debug = false;
		Save save = new Save();	
		Game game = null;
		
		if(debug) {
			Type type = Type.BUG;
			msg(type.getWeakness());
			return;
		}
		
		if(save.newGame) {
			msg("Bem vindo ao mundo de pokemon!\nPessoas referem-se a mim "
					+ "afetuosamente como Professor Pokémon. Esse mundo é hábitado vastamento por\n "
					+ "criaturas chamadas Pokemon. Para algumas pessoas pokemons são pets. Para"
					+ "outras, são para batalhas. \nMas primeiro, conte-me um pouco sobre você."
					+ "\nAgora me conte, você é um menino, ou uma menina?");
			String firstAnswer = nextLine();
			msg("Agora o seu nome. Me diga qual é?");
			String secondAnswer = nextLine();
			game = new Game(firstAnswer, secondAnswer, 0);
			msg("Um mundo de sonhos e aventuras pokemons lhe aguardam! Vamos lá!\n...............................");
			msg(game.getPlayerNick() + " foi teleportado para sua cidade, é hora de começar sua jornada!");
		} else 
			msg("Que bom te ver de novo!");
		while (true) {
			game.menu();
			String choose = nextLine();
			game.playerChoose(choose);
		}
	}
	private static Scanner scanner = new Scanner(System.in);
	public static void msg(String message) { System.out.println(message); }
	private static String nextLine() { return scanner.nextLine(); }
}