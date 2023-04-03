package Objects;

import GameTiles.Unit;

public class Range {
    double range;

    public static double range(Unit first, Unit second){
        return first.getPosition().distance(second.getPosition());
    }
}
