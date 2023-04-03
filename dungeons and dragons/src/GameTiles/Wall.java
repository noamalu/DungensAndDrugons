package GameTiles;

import Objects.Position;

public class Wall extends Tile{
    public Wall(Position position) {
        super(position, '#');
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }
}
