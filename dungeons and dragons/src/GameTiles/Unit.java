package GameTiles;

import Enemy.*;
import Game.MessageCallback;
import Objects.Position;
import Player.Player;
import VisitorPattern.Visitor;

import java.util.Random;

public abstract class Unit extends Tile implements Visitor {
	protected String name;
    protected int healthPool;
    protected int healthAmount;
    protected int attackPoints;
    protected int defensePoints;
    public MessageCallback messageCallback;


    protected Unit(Position position, char tile, String name, int healthCapacity, int attack, int defense) {
        super(position, tile);
        attackPoints = attack;
        defensePoints=defense;
        healthPool = healthCapacity;
        healthAmount =healthCapacity;
        this.name = name;
    }

    public abstract void gameTick();


    public void initialize(Position position, MessageCallback messageCallback){
        this.initialize(position);
        this.messageCallback =messageCallback;
    }
	
    protected int attack() {
        Random random = new Random();
        int attack = random.nextInt(this.getAttackPoints() + 1);
        return attack;
    }

    public int defend(){
        Random random = new Random();
        int defence = random.nextInt(this.getDefensePoints() + 1);
        return defence;
    }

	// Should be automatically called once the unit finishes its turn
    public abstract void processStep();



    public void setMessageCallback(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    // What happens when the unit dies
    public abstract void onDeath();

	// This unit attempts to interact with another tile.
    public void interact(Tile tile){
        tile.accept(this);
    }

    public abstract void visit(Player p);
    public abstract void visit(Enemy e);
    public void visit(Empty empty){
        updatePosition(empty);
    }
    public void visit(Wall wall){

    }

	// Combat against another unit.
    protected void battle(Unit u){
        messageCallback.send(this.getName() + " engaged in combat with " + u.getName());
        messageCallback.send(this.describe());
        messageCallback.send(u.describe());
        int attack = attack();
        messageCallback.send(this.getName() + " rolled " + attack + " attack points");
        int defend = u.defend();
        messageCallback.send(u.getName() + " rolled " + defend + " defence points");
        if (attack - defend > 0) {
            u.gotHit(attack - defend);
            messageCallback.send(this.getName() + " dealt " + (attack - defend) + " damage to " + u.getName());
        }
        messageCallback.send(healthAmount+" enemy health");
//        if(u.getHealthAmount() <= 0 ){
//            u.onDeath();
//        }
//        if(getHealthAmount() <= 0){
//            onDeath();
//        }
    }


    public int gotHitByCastAbility(int damage){
        int defence = defend();
        if ((getHealthAmount() - (damage-defence)) < 0) {
            setHealthAmount(0);
            onDeath();
        } else
            setHealthAmount(getHealthAmount() - (damage-defence));
        return defence;
    }

    protected void abilityDamage(Unit u, int abilityDamage, int defence){
        int DamageDone= abilityDamage-defence;
        messageCallback.send(u.getName() + " rolled " + defence + " defence points");
        messageCallback.send(String.format("%s hit %s for %d ability damage", getName(), u.getName(), DamageDone));
    }

    public String describe() {
        return String.format("%s\t\tHealth: %s\t\tAttack: %d\t\tDefense: %d", getName(), getHealth(), getAttackPoints(), getDefensePoints());
    }



    public void updatePosition(Tile tile){

        Position p = tile.getPosition();
        tile.setPosition(this.getPosition());
        this.setPosition(p);
    }

    public void gotHit(int damage){
        if ((getHealthAmount()-damage)< 0){
            setHealthAmount(0);
            onDeath();
        } else
            setHealthAmount(getHealthAmount()-damage);
    }



    public int getDefensePoints() {
        return defensePoints;
    }

    public int getAttackPoints(){
        return attackPoints;
    }

    public String getHealth(){
        return String.format("%d/%d", healthAmount, healthPool);
    }

    public int getHealthPool(){
        return healthPool;
    }

    public int getHealthAmount(){
        return healthAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealthPool(int healthPool) {
        this.healthPool = healthPool;
    }

    public void setHealthAmount(int healthAmount) {
        if (healthAmount < 0)
            this.healthAmount = 0;
        else this.healthAmount = Math.min(healthAmount, healthPool);
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }


    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }

    public MessageCallback getMessageCallback() {
        return messageCallback;
    }

    public String getName(){
        return name;
    }

}
