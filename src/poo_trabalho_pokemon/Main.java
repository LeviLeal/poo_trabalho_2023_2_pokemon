package poo_trabalho_pokemon;
import java.io.File;
import java.util.*;

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

abstract class Pokemon {
	String name;
	String type;
	int level;
	int hp;
	
	Pokemon(String name, String type, int level, int hp) {
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
	
	Player player;
	
	Game (String playerSex, String playerNick, int insigneas) {
		this.player = new Player(playerNick, playerSex, insigneas);
	}
	
	public String getPlayerNick() {
		return this.player.getNick();
	}
	
	public void menu () {
		Main.msg("===================\n"
				+ "| Avancar | MyInfo");
	}
	
	public void battleMenu () {
		Main.msg("=====================\n"
				+ "| Lutar | Fugir... |"
				+ "\n====================="
				+ "\n O que vai escolher?");
	}
	
	public void playerChoose(String choose) {
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

public class Main {
	public static void main (String[]args) {
		Save save = new Save();	
		Game game = null;
		
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
			game.playerChoose(null);
		}
		
	}
	
	private static Scanner scanner = new Scanner(System.in);
	public static void msg(String message) { System.out.println(message); }
	private static String nextLine() { return scanner.nextLine(); }
}