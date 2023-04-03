package Player;
import Enemy.Enemy;
import Objects.Position;
import Objects.Range;
import java.util.ArrayList;
import java.util.List;

public class Warrior extends Player {
    private int abilityCooldown;
    private int remainingCooldown;
    public int getAbilityCooldown() {
        return abilityCooldown;
    }

    public int getRemainingCooldown() {
        return remainingCooldown;
    }

        public Warrior(Position pos, String name, int healthPool, int attackPoints, int defencePoints,
                   int abilityCooldown) {
            super(pos, name, healthPool, attackPoints, defencePoints);
            this.abilityCooldown = abilityCooldown;
            remainingCooldown = 0;
            skill = String.format("%s used Avenger's Shield, healing for %d.", getName(), 10*getDefensePoints());

        }
    public String describe() {
        return super.describe()+ String.format("\tCooldown: %d/%d"
                , getRemainingCooldown(), getAbilityCooldown());
    }
    protected void levelUp(){
        super.levelUp();
        remainingCooldown =0;
        setHealthPool(getHealthPool()+5*getLevel());
        setAttackPoints(getAttackPoints()+5*getLevel());
        setDefensePoints(getDefensePoints()+getLevel());
    }

    @Override
    public void castAbility(List<Enemy> enemies) {
        super.streamAbility();
        if (remainingCooldown > 0)
            return;
        else {
            remainingCooldown = abilityCooldown+1;
            setHealthAmount(Math.min(getHealthAmount() + (10 * getDefensePoints()), getHealthPool()));
            if (enemies != null) {
                List<Enemy> range3 = new ArrayList<Enemy>();
                for (Enemy enemy : enemies) {
                    if (Range.range(this, enemy) < 3)
                        range3.add(enemy);
                }
                if (range3.size() > 0) {
                    int Index = (int) Math.floor(Math.random() * (range3.size()));
                    Enemy enemy = range3.get(Index);
                    int hit = (int) (this.getHealthPool() * 0.1);
                    int defence = enemy.gotHitByCastAbility(hit);
                    abilityDamage(enemy, hit, defence);
                    if (this.getHealthAmount() + (10 * this.getDefensePoints()) <= this.getHealthPool()) {
                        this.setHealthAmount(this.getHealthAmount() + (10 * this.getDefensePoints()));
                    }
                }
            }
        }
    }
        public void gameTick () {
            if (remainingCooldown > 0)
                remainingCooldown = remainingCooldown - 1;
        }


}
