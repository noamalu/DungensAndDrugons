package Enemy;

import Objects.Position;
import Player.*;

import static Objects.Range.range;

public class Monster extends Enemy {
    private final int visionRange;

    public Monster(Position position, char tile, String name,
                   int healthCapacity, int attack, int defense, int experience, int visionRange) {
        super(position, tile, name, healthCapacity, attack, defense, experience);
        this.visionRange =visionRange;
    }

    @Override
    public Position onTurn(Player player){
        Position moveTo = position;
        if(range(this, player)<visionRange) {
            return chase(player, moveTo);
        }else
            return randomMove(moveTo);
    }

    protected Position chase(Player player, Position moveTo){
        int dx = this.position.getX() - player.getPosition().getX();
        int dy = this.position.getY() - player.getPosition().getY();
        if (Math.abs(dx) > Math.abs(dy)){
            if (dx > 0) {
                return moveTo.moveLeft();
            }
            else {

                return moveTo.moveRight();
            }
        }else{
            if (dy > 0) moveTo.moveUp();
            else return moveTo.moveDown();
        }
        return moveTo;
    }

    protected Position randomMove(Position moveTo){
        int direct = (int)(Math.random()*(5))+1;
        switch (direct) {
            case 1 -> {
                return moveTo.moveUp();
            }
            case 2 -> {
                return moveTo.moveDown();
            }
            case 3 -> {
                return moveTo.moveLeft();
            }
            case 4 -> {
                return moveTo.moveRight();
            }
        }
        return moveTo;
    }
    public int getVisionRange() {
        return visionRange;
    }

    @Override
    public void gameTick() {

    }

    @Override
    public String describe() {
        return super.describe() + String.format("\t\tVision range: %d", getVisionRange()) ;
    }
}
