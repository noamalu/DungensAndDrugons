package Player;

import Enemy.Enemy;
import Objects.Position;
import Objects.Range;

import java.util.ArrayList;
import java.util.List;

public class Hunter extends Player{

     int range;
     int arrows;
     int tickCount;

    public Hunter(Position pos, String name, int healthPool, int attackPoints, int defencePoints,
                  int range) {
        super(pos, name, healthPool, attackPoints, defencePoints);
        this.range= range;
        this.arrows = 10*getLevel();
        this.tickCount=0;
        skill = String.format("%s fired an arrow at", getName());
    }
    public String describe(){
        return super.describe()+ String.format("\t range: %d\t arrows: %d\t tickCount:%d\t "
                ,getRange(), getArrows(),getTickCount());
    }
    protected void levelUp() {
        super.levelUp();
        this.arrows= arrows+10*getLevel();
        setAttackPoints(getAttackPoints()+2*getLevel());
        setDefensePoints(getDefensePoints()+getLevel());
    }
    public void gameTick() {
        if (tickCount==10){
            arrows = arrows+getLevel();
            tickCount = 0;
        }
        else
            tickCount = tickCount+1;
    }
    @Override
    public void castAbility(List<Enemy> enemies) {
        if (arrows==0)
            return;
        if (enemies != null) {
            Enemy closestEnemy = enemies.get(0);

            for (Enemy enemy : enemies) {
                if (Range.range(this, enemy) < Range.range(this, closestEnemy))
                    closestEnemy = enemy;
            }


            if (!(Range.range(this, closestEnemy) < this.range)) {
                skill = "Ygritte tried to shoot an arrow but there were no enemies in range.";
                super.streamAbility();
            }else{
                skill+=" "+closestEnemy.getName();
                super.streamAbility();
                int defence =closestEnemy.gotHitByCastAbility(getAttackPoints());
                abilityDamage(closestEnemy, this.getAttackPoints(), defence);
                arrows = arrows - 1;
            }

        }
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getArrows() {
        return arrows;
    }

    public void setArrows(int arrows) {
        this.arrows = arrows;
    }

    public int getTickCount() {
        return tickCount;
    }

    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

}
