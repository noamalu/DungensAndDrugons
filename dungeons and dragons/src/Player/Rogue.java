package Player;
import Enemy.Enemy;
import Objects.Position;
import Objects.Range;

import java.util.ArrayList;
import java.util.List;

public class Rogue extends Player {
    public int getCost() {
        return cost;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    private int cost;
    private int currentEnergy;
    public Rogue(Position pos, String name, int healthPool, int attackPoints, int defencePoints,
                int cost) {
        super(pos, name, healthPool, attackPoints, defencePoints);
        this.cost = cost;
        this.currentEnergy = 100;
        skill = String.format("%s cast Fan of Knives.", getName());
    }
    public String describe() {
        return super.describe() + String.format("\tCost: %d,\t currentEnergy: %d"
                ,getCost(),getCurrentEnergy());
    }
    protected void levelUp() {
        super.levelUp();
        currentEnergy=100;
        setAttackPoints(getAttackPoints()+3*getLevel());
    }
    public void gameTick() {
        currentEnergy=Math.min(currentEnergy+10, 100);
    }
    public void castAbility(List<Enemy> enemies) {
        super.streamAbility();
        List<Enemy> enemies1 = new ArrayList<>(enemies);
        if (enemies1 != null && currentEnergy>=cost) {
            for (Enemy enemy : enemies1) {
                if (Range.range(this, enemy) < 2) {
                    int defence = enemy.gotHitByCastAbility(this.getAttackPoints());
                    abilityDamage(enemy, this.getAttackPoints(), defence);

                }
            }
            currentEnergy = currentEnergy-cost;

        }

    }
}
