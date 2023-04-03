package Game;

import Enemy.*;
import GameTiles.Empty;
import GameTiles.Wall;
import Objects.Position;
import Player.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TileFactory {
    private List<Supplier<Player>> playersList;
    private Player selected;

    public TileFactory(){
        playersList = initPlayers(new Position(0, 0));
    }

    private Map<Character, Supplier<Enemy>> initEnemies(Position pos) {
        List<Supplier<Enemy>> enemies = Arrays.asList(
                () -> new Monster(pos,'s', "Lannister Solider", 80, 8, 3,25, 3),
                () -> new Monster(pos,'k', "Lannister Knight", 200, 14, 8, 50,   4),
                () -> new Monster(pos,'q', "Queen's Guard", 400, 20, 15, 100,  5),
                () -> new Monster(pos,'z', "Wright", 600, 30, 15,100, 3),
                () -> new Monster(pos,'b', "Bear-Wright", 1000, 75, 30, 250,  4),
                () -> new Monster(pos,'g', "Giant-Wright",1500, 100, 40,500,   5),
                () -> new Monster(pos,'w', "White Walker", 2000, 150, 50, 1000, 6),
                () -> new Boss(pos,'M', "The Mountain", 1000, 60, 25,  500, 6, 5),
                () -> new Boss(pos,'C', "Queen Cersei", 100, 10, 10,1000, 1, 8),
                () -> new Boss(pos,'K', "Night's King", 5000, 300, 150, 5000, 8, 3),
                () -> new Trap(pos,'B', "Bonus Trap", 1, 1, 1, 250,  1, 10),
                () -> new Trap(pos,'Q', "Queen's Trap", 250, 50, 10, 100, 3, 10),
                () -> new Trap(pos,'D', "Death Trap", 500, 100, 20, 250, 1, 10)
        );

        return enemies.stream().collect(Collectors.toMap(s -> s.get().getTile(), Function.identity()));
    }

    private List<Supplier<Player>> initPlayers(Position pos) {
        return Arrays.asList(
                () -> new Warrior(pos,"Jon Snow", 300, 30, 4, 3),
                () -> new Warrior(pos,"The Hound", 400, 20, 6, 5),
                () -> new Mage(pos,"Melisandre", 100, 5, 1, 300, 30, 15, 5, 6),
                () -> new Mage(pos,"Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4),
                () -> new Rogue(pos,"Arya Stark", 150, 40, 2, 20),
                () -> new Rogue(pos,"Bronn", 250, 35, 3, 50),
                () -> new Hunter(pos,"Ygritte", 220, 30, 2, 6)
        );
    }

    public List<Player> listPlayers(){
        return playersList.stream().map(Supplier::get).collect(Collectors.toList());
    }

    // TODO: Add additional callbacks of your choice

    public Enemy produceEnemy(char tile, Position position) {
        return initEnemies(position).get(tile).get();
    }

    public Player producePlayer(Position position, int idx){
        return initPlayers(position).get(idx).get();
    }

    public Empty produceEmpty(Position position){
        return new Empty(position);
    }

    public Wall produceWall(Position position){
        return new Wall(position);
    }
}
