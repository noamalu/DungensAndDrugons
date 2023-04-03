package Game;



import Enemy.*;
import GameTiles.Empty;
import GameTiles.Tile;
import Objects.Position;
import Player.*;
import groovyjarjarasm.asm.util.Printer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**deco: this class will run the game, receive input from the player,
 manage the turn based movement*/
public class GameManager {
	private List<Enemy> enemies;
	private int countLevels;
	private Player player;
	private Scanner s = new Scanner(System.in);
	private ArrayList<Board> boardsPerLevel;
	private Board board;
	
	MessageCallback messageCallback = System.out::println;

	EnemyDeathCallback enemyDeathCallback = this::onEnemyDeath;

	PlayerDeathCallBack playerDeathCallBack = this::onPlayerDeath;

	public GameManager() {
	}


	/** The first load of the game will open a menu of players to choose from;
	 * an equivalent to creating a new game manager*/
	public GameManager(String path) {
		boardsPerLevel = new ArrayList<>();//boards of all levels
		countLevels=0;
		//present options to the user
		List<Player> options = new ArrayList<>();
		Position falsePos = new Position(-1, -1);
		options.add(new Warrior(falsePos,"Jon Snow", 300, 30, 4, 3));
		options.add(new Warrior(falsePos, "The Hound", 400, 20, 6, 5));
		options.add(new Mage(falsePos,"Melisandre", 100, 5, 1, 300, 30, 15, 5, 6));
		options.add(new Mage(falsePos,"Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4));
		options.add(new Rogue(falsePos,"Arya Stark", 150, 40, 2, 20));
		options.add(new Rogue(falsePos,"Bronn", 250, 35, 3, 50));
		options.add(new Hunter(falsePos, "Ygritte", 220, 30, 2, 6));
		String playerOptions = "Select player:\n";
		//print options:
		int optionInd =1;
		for (Player p:
			options) {
			playerOptions += optionInd + ". " + p.describe() + "\n";
			optionInd++;
		}
		messageCallback.send(playerOptions);

		//receive user's choice
		char def = 'F';
		String character = s.next();
		if(character.length()==1) def =character.charAt(0);

		while ((character.length()!=1) || def<49 || def>55) character= s.next();
		try {
			int choice = Integer.parseInt(def+"");
			List<String> levels = Files.list(Paths.get(path)).sorted().map(Path::toString).collect(Collectors.toList());
			//System.out.println(levels.get(0));
			//what to do and how to use the array?
			for (String level : levels) boardsPerLevel.add(new Board(level, choice-1));

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		levelInitializer();
	}

	public void levelInitializer() {
		boolean continueGame = (boardsPerLevel.size()>countLevels);
		if(!continueGame) return;
		board = boardsPerLevel.get(countLevels);
		player = board.getOnePlayer();
		player.initialize(this.board.getPlayerPosition(), messageCallback, playerDeathCallBack);
		enemies = board.getEnemies();
		for (Enemy e :
			enemies) {
			//enemyDeathCallback.call(e);
			e.initialize(e.getPosition(), messageCallback, enemyDeathCallback);
		}
	}

	public void Play() {
		//boolean continueGame = (boardsPerLevel.size()>countLevels);
		while (boardsPerLevel.size()>countLevels) {
			if (playLevel()) {
				countLevels++;
				levelInitializer();
			} else {
				messageCallback.send("You Lost.");
				break;
			}
		}
		if (countLevels == boardsPerLevel.size())
			messageCallback.send("you won");
	}

	public void onEnemyDeath(Enemy enemy) {
		player.onKill(enemy);
		enemies.remove(enemy);
		board.updateEnemyPos(Position.at(-1,-1), enemy, true);
	}

	private void onPlayerDeath() {
		player.setTile('X');
	}


	public void Turn(char move) {
		if(player.getHealthAmount()!=0)
			PlayerTurn(move);
		player.gameTick();
		if (!enemies.isEmpty())
			EnemiesTurn();
//		if (enemies.isEmpty()) {
//			// won = true;
//		}
	}

	public void PlayerTurn(char move) {
		switch (move) {
		case 'w'://need to think how to update position inside the list
			board.updatePlayerPos(player.getPosition().moveUp());
			break;
		case 's':
			board.updatePlayerPos(player.getPosition().moveDown());
			break;
		case 'a':
			board.updatePlayerPos(player.getPosition().moveLeft());
			break;
		case 'd':
			board.updatePlayerPos(player.getPosition().moveRight());
			break;
		case 'q'://does nothing
			break;
		case 'e':
			player.castAbility(enemies);
			break;
		}
	}


	public void EnemiesTurn() {
		try {
			for (Enemy enemy : enemies) {
				Position newPos = enemy.onTurn(player);
				if(!newPos.equals(Position.at(-1,-1)))board.updateEnemyPos(newPos, enemy, false);
				enemy.gameTick();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public boolean playLevel() {
		//boolean levelIsOver = ;
		while (!(enemies.isEmpty() || player.getHealthAmount() <= 0)) {
			messageCallback.send(board.toString());
			messageCallback.send(player.describe());
			char move = s.next().charAt(0);
			Turn(move);
		}
		return player.getHealthAmount()>0;
	}



}
