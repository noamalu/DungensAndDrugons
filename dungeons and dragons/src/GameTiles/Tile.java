package GameTiles;

import Objects.Position;
import VisitorPattern.Visited;

public abstract class Tile implements Comparable<Tile>, Visited {

    protected char tile;
    protected Position position;

    public Tile(Position position, char tile){
        this.position = position;
        this.tile = tile;
    }

    protected void initialize(Position position){
        this.position = position;
    }

    public char getTile() {
        return tile;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract void accept(Unit unit);

    @Override
    public int compareTo(Tile tile) {
        return getPosition().compareTo(tile.getPosition());
    }

    @Override
    public String toString() {
        return String.valueOf(tile);
    }

    public void setTile(char tile) {
        this.tile = tile;
    }
}
