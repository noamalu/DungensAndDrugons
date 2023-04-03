package Player;

import Enemy.Enemy;
import Objects.Position;
import Objects.Range;

import java.util.ArrayList;
import java.util.List;

public class Mage extends Player {


    private int manaPool;
    private int currentMana;
    private int manaCost;
    private int spellPower;
    private int hitCount;
    private int abilityRange;

    public Mage(Position pos, String name, int healthPool, int attackPoints, int defencePoints,
                   int manaPool, int manaCost, int spellPower, int hitCount, int abilityRange) {
        super(pos, name, healthPool, attackPoints, defencePoints);
        this.manaPool=manaPool;
        this.currentMana=manaPool/4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitCount= hitCount;
        this.abilityRange=abilityRange;
        skill = String.format("%s cast Blizzard.", getName());
    }
    public String describe(){
        return super.describe()+ String.format("\t manaPool: %d\t currentMana: %d\t manaCost:%d\t spellPower:%d\t hitCount:%d\t abilityRange:%d\t "
                ,getManaPool(), getCurrentMana(),getManaCost(),getSpellPower(),getHitCount(),getAbilityRange());
    }
    protected void levelUp() {
        super.levelUp();
        manaPool= manaPool+ (25*getLevel());
        currentMana = Math.min(currentMana+(manaPool/4), manaPool);
        spellPower=spellPower+(10*getLevel());
    }


    public void gameTick() {
        currentMana= Math.min(manaPool,currentMana+getLevel());
    }
    public void castAbility(List<Enemy> enemies) {
        super.streamAbility();
        if (currentMana<manaCost)
            return;
        currentMana = currentMana-manaCost;
        int hits = 0;
        if (enemies.size()!=0) {
            List<Enemy> range = new ArrayList<Enemy>();
            for (Enemy enemy : enemies) {
                if (Range.range(this, enemy) < abilityRange)
                    range.add(enemy);
            }
            while (hits < hitCount) {
                int Index = (int) Math.floor(Math.random() * (range.size()));
                Enemy enemy = range.get(Index);
                int defence = enemy.gotHitByCastAbility(spellPower);
                abilityDamage(enemy, spellPower, defence);

                hits = hits + 1;

            }
        }
    }
    public int getManaPool() {
        return manaPool;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getSpellPower() {
        return spellPower;
    }

    public int getHitCount() {
        return hitCount;
    }

    public int getAbilityRange() {
        return abilityRange;
    }
}
