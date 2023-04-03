package Enemy;

import Game.HeroicUnit;
import Objects.Position;
import Objects.Range;
import Player.Player;

import java.util.List;

import static Objects.Range.range;

public class Boss extends Monster implements HeroicUnit {
    private int abilityFrequency;
    private int combatTicks;
    private Double range;
    private Player player;


    public Boss(Position position, char tile, String name, int healthCapacity,
                int attack, int defense, int experience, int visionRange, int abilityFrequency) {
        super(position, tile, name, healthCapacity, attack, defense, experience, visionRange);
        this.abilityFrequency =abilityFrequency;
        combatTicks =0;
    }
    public void streamAbility(){
        messageCallback.send(String.format("%s shoots %s for %d attack points" ,getName(),player.getName(), getAttackPoints()));
    }
    @Override
    public void castAbility(List<Enemy> enemies) {
        if (range < getVisionRange()) {
            streamAbility();
            int defence = player.gotHitByCastAbility(this.getAttackPoints());
            abilityDamage(player, this.getAttackPoints(), defence);
        }
    }

    @Override
    public Position onTurn(Player player) {
        Position moveTo = position;
        range= Range.range(player, this);
        this.player = player;
        if(range(this, player)<getVisionRange()){
            if(combatTicks==abilityFrequency){
                combatTicks=0;
                castAbility(null);
                return moveTo;
            }else{
                combatTicks++;
                return chase(player, moveTo);
            }
        }else{
            combatTicks=0;
            return randomMove(moveTo);
        }
    }
}
