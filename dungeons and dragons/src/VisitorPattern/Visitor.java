package VisitorPattern;

import Enemy.Enemy;
import GameTiles.Empty;
import GameTiles.Wall;
import Player.Player;

public interface Visitor {
    public void visit(Player player);
    public void visit(Enemy enemy);
    public void visit(Empty empty);
    public void visit(Wall wall);

}
