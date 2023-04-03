package Enemy;

import Objects.Position;
import Player.*;

import static Objects.Range.range;

public class Trap extends Enemy {
    private final int visibilityTime;
    private final int invisibilityTime;
    private int ticksCount;
    private boolean visible;
    private char defaultView;

    public Trap(Position position, char tile, String name, int healthCapacity, int attackPoints,
                int defencePoints, int experience, int visibilityTime, int invisibilityTime) {
        super(position, tile, name,healthCapacity,attackPoints,defencePoints,experience);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        visible = true;
        ticksCount=0;
        defaultView=tile;
    }

    @Override
    public void gameTick() {
    }

    @Override
    public Position onTurn(Player player){
        visible = (ticksCount<visibilityTime);
        if(!visible) tile='.';
        else tile=defaultView;
        if(ticksCount==(visibilityTime+invisibilityTime)) ticksCount = 0;
        else ticksCount++;
        if (range(this, player)<2)return position;
        return new Position(-1,-1);
    }

}
