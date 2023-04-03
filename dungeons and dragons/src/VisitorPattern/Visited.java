package VisitorPattern;

import Enemy.Enemy;
import GameTiles.Unit;
import Player.Player;

public interface Visited {
    public void accept(Unit unit);
}
