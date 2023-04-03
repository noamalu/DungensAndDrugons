package Test;
import Enemy.*;
import Game.*;
import GameTiles.Tile;
import GameTiles.Unit;
import Objects.*;
import Player.*;
import org.junit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TestGame {
    Board b;
    MessageCallback messageCallback = System.out::println;
    PlayerDeathCallBack playerDeathCallBack = this::onPlayerDeath;
    EnemyDeathCallback enemyDeathCallback = this::onEnemyDeath;
    private void onPlayerDeath() {
        player.setTile('X');
    }
    public void onEnemyDeath(Enemy enemy) {
        player.onKill(enemy);
        enemies.remove(enemy);
        b.updateEnemyPos(Position.at(-1,-1), enemy, true);
    }

    List<String> levels = null;
    Player player;
    private List<Enemy> enemies;
    int PlayerType;

    @Before
    public void SetUp(){
        String path = "C:\\Users\\malul\\OOPAssignment3\\levels_dir";
        try {
            levels = Files.list(Paths.get(path)).sorted().map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Player> options = new ArrayList<>();
        Position falsePos = new Position(-1, -1);
        options.add(new Warrior(falsePos,"Jon Snow", 300, 30, 4, 3));
        options.add(new Warrior(falsePos, "The Hound", 400, 20, 6, 5));
        options.add(new Mage(falsePos,"Melisandre", 100, 5, 1, 300, 30, 15, 5, 6));
        options.add(new Mage(falsePos,"Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4));
        options.add(new Rogue(falsePos,"Arya Stark", 150, 40, 2, 20));
        options.add(new Rogue(falsePos,"Bronn", 250, 35, 3, 50));
        options.add(new Hunter(falsePos, "Ygritte", 220, 30, 2, 6));

        Random rand = new Random();
        int randP  = rand.nextInt(7);
        int randL  = rand.nextInt(4);
        PlayerType = randP;
        b = new Board(levels.get(randL), randP);
        enemies = b.getEnemies();
        for (Enemy e :
                enemies) {
            //enemyDeathCallback.call(e);
            e.initialize(e.getPosition(), messageCallback, enemyDeathCallback);
        }

        player = b.getOnePlayer();
        System.out.println(player.describe());
        player.initialize(b.getPlayerPosition(), messageCallback, playerDeathCallBack);
        //System.out.println(b.toString());
    }


    private void PlayerTurn(char move) {
        switch (move) {
            case 'w'://need to think how to update position inside the list
                b.updatePlayerPos(player.getPosition().moveUp());
                break;
            case 's':
                b.updatePlayerPos(player.getPosition().moveDown());
                break;
            case 'a':
                b.updatePlayerPos(player.getPosition().moveLeft());
                break;
            case 'd':
                b.updatePlayerPos(player.getPosition().moveRight());
                break;
            case 'q'://does nothing
                break;
            case 'e':
                player.castAbility(enemies);
                break;
        }
    }

    @Test
    public void TestMoves(){
        Position pos = player.getPosition().dupe();
        int repeat = 40;
        String sequence = "dddwsdddwwwwwwaaaaaaaaaaaa";
        for (int i = 0; i<repeat; i++) {
            Tile t = b.tileAt(Position.at(pos.getX() + 1, pos.getY()));
            char c = sequence.charAt(i % sequence.length());
            if (t.getTile() == '.' && c == 'd') {
                pos = moveRight(pos);
                messageCallback.send("Test moved right");
            } else if (t.getTile() == '#' && c == 'd') TestCollapse('d');
            else if (c == 'd')TestBattle('d', t);

            t = b.tileAt(Position.at(pos.getX(), pos.getY() - 1));
            if (t.getTile() == '.' && c == 'w') {
                pos = moveUp(pos);
                messageCallback.send("Test moved up");
            } else if (t.getTile() == '#' && c == 'w') TestCollapse('w');
            else if (c == 'w') TestBattle('w', t);


            t = b.tileAt(Position.at(pos.getX() - 1, pos.getY()));
            if (t.getTile() == '.' && c == 'a') {
                pos = moveLeft(pos);
                messageCallback.send("Test moved left");
            } else if (t.getTile() == '#' && c == 'a') TestCollapse('a');
            else if (c == 'a') TestBattle('a', t);


            t = b.tileAt(Position.at(pos.getX(), pos.getY() + 1));
            if (t.getTile() == '.' && c == 's') {
                pos = moveDown(pos);
                messageCallback.send("Test moved down");
            } else if (t.getTile() == '#' && c == 's') TestCollapse('s');
            else if (c == 's') TestBattle('s', t);

        }
        //System.out.println(b.toString());
        TestEnemies();
        //System.out.println(b.toString());

    }

    //move right
    private Position moveRight(Position pos) {
        PlayerTurn('d');
        Assert.assertEquals(pos.getX() + 1, player.getPosition().getX());
        Assert.assertEquals(pos.getY(), player.getPosition().getY());
        pos = player.getPosition();
        return pos;
        //System.out.println(b.toString());
    }

    //move up
    private Position moveUp(Position pos) {
        PlayerTurn('w');
        Assert.assertEquals(pos.getY() - 1, player.getPosition().getY());
        Assert.assertEquals(pos.getX(), player.getPosition().getX());
        pos = player.getPosition();
        return pos;

        //System.out.println(b.toString());
    }

    //move left
    private Position moveLeft(Position pos) {
        PlayerTurn('a');
        Assert.assertEquals(pos.getX() - 1, player.getPosition().getX());
        Assert.assertEquals(pos.getY(), player.getPosition().getY());
        pos = player.getPosition();
        return pos;

        //System.out.println(b.toString());
    }

    //move down
    private Position moveDown(Position pos) {
        PlayerTurn('s');
        Assert.assertEquals(pos.getY() + 1, player.getPosition().getY());
        Assert.assertEquals(pos.getX(), player.getPosition().getX());
        pos = player.getPosition();
        return pos;

        //System.out.println(b.toString());
    }

    private void TestCollapse(char to){
        //move into wall
        Position pos = player.getPosition().dupe();
        PlayerTurn(to);
        Assert.assertEquals(pos, player.getPosition());
        //System.out.println(b.toString());
        messageCallback.send("Test moved into wall");
    }

    private void TestBattle(char to, Tile enemy){
        //move into enemy
        Enemy e = (Enemy) enemy;
        Position pos = player.getPosition().dupe();
        int health = e.getHealthAmount();
        PlayerTurn(to);
        Assert.assertTrue(pos.equals(player.getPosition())|| pos.equals(e.getPosition()));
        if (pos.equals(e.getPosition()))
            Assert.assertEquals(0, e.getHealthAmount());
        Assert.assertTrue(e.getHealthAmount()<=health);

        messageCallback.send("Test moved into enemy");

        if( pos.equals(player.getPosition()) && player.getHealthAmount()>0){
            castAbilityTests();
        }
        //System.out.println(b.toString());
    }

    @Test
    public void castAbilityTests(){

        switch (PlayerType) {
            case 0, 1 -> {
                Warrior w = (Warrior) player;
                int cool = w.getAbilityCooldown();
                PlayerTurn('e');
                if(cool!=w.getAbilityCooldown() && enemies!=null)
                    Assert.assertNotEquals(cool, w.getAbilityCooldown());
            }
            case 2, 3 -> {
                Mage m = (Mage) player;
                int mana = m.getCurrentMana();
                PlayerTurn('e');
                if(mana>=m.getManaCost() && enemies!=null)
                    Assert.assertNotEquals(mana, m.getCurrentMana());
            }
            case 4, 5 -> {
                Rogue r = (Rogue) player;
                int energy = r.getCurrentEnergy();
                PlayerTurn('e');
                if(energy!=0 && enemies!=null)
                    Assert.assertNotEquals(energy, r.getCurrentEnergy());
            }

            case 6 -> {
                Hunter h = (Hunter) player;
                int arrows = h.getArrows();
                int ticks = h.getTickCount();
                PlayerTurn('e');
                Assert.assertNotEquals(arrows, h.getArrows());
                if(ticks!=0 && !HuntersRange() && enemies!=null)
                    Assert.assertNotEquals(ticks, h.getTickCount());

            }
        }
        messageCallback.send("Test cast ability performed");

    }

    private boolean HuntersRange(){
        Enemy closestEnemy = enemies.get(0);

        for (Enemy enemy : enemies) {
            if (Range.range(player, enemy) < Range.range(player, closestEnemy))
                closestEnemy = enemy;
        }
        return !(Range.range(player, closestEnemy) < 6);
    }
    private boolean ClearRange(Unit unit){
        Tile t = b.tileAt(Position.at(unit.getPosition().getX()+1, unit.getPosition().getY()));
        if(t.getTile()!='.') return false;

        t = b.tileAt(Position.at(unit.getPosition().getX()-1, unit.getPosition().getY()));
        if(t.getTile()!='.') return false;

        t = b.tileAt(Position.at(unit.getPosition().getX()+1, unit.getPosition().getY()+1));
        if(t.getTile()!='.') return false;

        t = b.tileAt(Position.at(unit.getPosition().getX(), unit.getPosition().getY()+1));
        if(t.getTile()!='.') return false;

        t = b.tileAt(Position.at(unit.getPosition().getX(), unit.getPosition().getY()-1));
        if(t.getTile()!='.') return false;

        t = b.tileAt(Position.at(unit.getPosition().getX()-1, unit.getPosition().getY()-1));
        if(t.getTile()!='.') return false;

        t = b.tileAt(Position.at(unit.getPosition().getX()-1, unit.getPosition().getY()+1));
        if(t.getTile()!='.') return false;

        t = b.tileAt(Position.at(unit.getPosition().getX()+1, unit.getPosition().getY()-1));
        return t.getTile() == '.';
    }

    private void EnemiesTurn() {
        try {
            for (Enemy enemy : enemies) {
                Position newPos = enemy.onTurn(player);
                if(!newPos.equals(Position.at(-1,-1)))b.updateEnemyPos(newPos, enemy, false);
                enemy.gameTick();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Test
    public void TestEnemies(){

        for (Enemy e:enemies
             ) {
            Position pos = e.getPosition().dupe();

            if(!(e instanceof Trap)){
                Monster monster = (Monster)e;
                if(Range.range(monster, player)<monster.getVisionRange() && ClearRange(player) && ClearRange(monster)){
                    PlayerTurn('q');
                    //System.out.println(b.toString());
                    EnemiesTurn();
                    //System.out.println(b.toString());

                    Assert.assertNotEquals(pos, monster.getPosition());

                    messageCallback.send(String.format("Test enemy %s moved", monster.getName()));
                }
            }

        }

    }



    @Test
    public void deadPlayer(){
        onPlayerDeath();
        System.out.println(b.toString());
        Assert.assertEquals(b.tileAt(player.getPosition()).getTile(), 'X');

    }

    @Test
    public void deadEnemies(){
        List<Enemy> enemies1 = new ArrayList<>(enemies);
        for (Enemy e:
                enemies1) {
            Position pos = e.getPosition().dupe();

            //kill temporarily enemy
            onEnemyDeath(e);
            Assert.assertEquals(b.tileAt(pos).getTile(), '.');
        }
    }

}
