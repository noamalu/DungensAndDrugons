package Game;

import Enemy.Enemy;
import GameTiles.Empty;
import GameTiles.Tile;
import GameTiles.Wall;
import Objects.Position;
import Objects.Range;
import Player.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private List<Tile> tiles;
    private List<Enemy> enemies;
    private TileFactory tileFactory = new TileFactory();
    int column;

    public Player getOnePlayer() {
        return OnePlayer;
    }

    private Player OnePlayer;

    public void GameInitializer(String path, int p) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.err.println("level reading failed");
        }
        int rows = lines.size();
        column = lines.get(0).length();
        char[][] arr = new char[column][rows];
        for (int i = 0; i < rows ; i++){
            for (int j = 0; j < column; j++)
                arr[j][i] = lines.get(i).charAt(j);
        }
        for (int i  = 0;i < arr.length; i++)
        {
            for (int j = 0; j < arr[i].length; j++) {
                Position pos = new Position(i, j);
                if (arr[i][j] == '.')
                    tiles.add(new Empty(pos));
                else if (arr[i][j] == '#')
                    tiles.add(new Wall(pos));
                else if (arr[i][j] == '@') {
                    Player player = tileFactory.producePlayer(new Position(i, j),p);
                    OnePlayer = player;
                    tiles.add(player);
                }
                else{
                    char x = arr[i][j];
                    Enemy enemy = tileFactory.produceEnemy(x,new Position(i, j));
                    enemies.add(enemy);
                    tiles.add(enemy);
                }
            }
        }
    }


    public Board(String path, int p){
        enemies = new ArrayList<>();
        tiles = new ArrayList<>();
        GameInitializer(path, p);
    }


    public Tile tileAt(Position pos) {
        Tile output= null;
        for (Tile t : tiles) {
            if (t.getPosition().equals(pos)) {
                output = t;
                break;
            }
        }
        if(output==null) throw new IllegalArgumentException("no such tile");
        return output;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public String toString() {
        tiles = tiles.stream().sorted().collect(Collectors.toList());
        // TODO: Implement me
        String output ="";

        for (Tile tile:
             tiles) {
            if(tile.getPosition().getX()==0) output+="\n";
            output+=tile.getTile();
        }
        return output;
    }
    public Position getPlayerPosition() {
        Player p = getOnePlayer();
        return p.getPosition();
    }

    public void updatePlayerPos(Position pos){
        getOnePlayer().interact(tileAt(pos)); //use visitor pattern to move

    }

    public void updateEnemyPos(Position pos, Enemy enemy, boolean dead){
        if(dead){
            Empty deadSpot = new Empty(enemy.getPosition());
            enemy.setPosition(pos);
            tiles.remove(enemy);
            tiles.add(deadSpot);
            if(Range.range(getOnePlayer(), enemy)<= 1 && enemy.getPosition().getY()==getOnePlayer().getPosition().getY()){
                getOnePlayer().updatePosition(deadSpot); //manually swap positions
            }
        }else
            enemy.interact(tileAt(pos));//use visitor pattern to move
    }

}