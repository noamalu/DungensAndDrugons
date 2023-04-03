package GameTiles;

import Objects.Position;

public class Empty extends Tile {

    public Empty(Position p) {
        super(p, '.');
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }
}
