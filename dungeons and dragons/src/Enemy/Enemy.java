package Enemy;

import Game.EnemyDeathCallback;
import Game.MessageCallback;
import GameTiles.Unit;
import Objects.Position;
import Player.*;

public abstract class Enemy extends Unit {
    private final int experience;
    public EnemyDeathCallback enemyDeathCallback;

    protected Enemy(Position position,char tile, String name, int healthCapacity, int attack, int defense, int experience) {
        super(position, tile, name, healthCapacity, attack, defense);
        this.experience =experience;
    }

    public Enemy initialize(Position position, MessageCallback messageCallback, EnemyDeathCallback enemyDeathCallback){
        super.initialize(position, messageCallback);
        this.enemyDeathCallback = enemyDeathCallback;
        return this;
    }
    public abstract void gameTick();
    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }

    @Override
    public void processStep() {

    }

    public void addExp(int exp){}
    @Override
    public void onDeath() {
        enemyDeathCallback.call(this);
    }

    @Override
    public void visit(Player p) {
        this.battle(p);
    }

    @Override
    public void visit(Enemy e) {
        //nothing happens.
    }




    public int getExperience() {
        return experience;
    }

    @Override
    public String describe() {
        return super.describe() + String.format("\t\tExperience value: %d", getExperience()) ;
    }

    public abstract Position onTurn(Player player);
}
