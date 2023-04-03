package Objects;

import java.util.Objects;

public class Position implements Comparable<Position>{

    private int positionX;
    private int positionY;

    public Position(int x, int y){
        positionX =x;
        positionY =y;
    }

    public static Position at(int x, int y) {
        return new Position(x,y);
    }
    public Position dupe(){
        return new Position(positionX, positionY);
    }

    @Override
    public int compareTo(Position position) {
        if(positionY>position.positionY) return 1;
        else if(positionY == position.positionY){
            if(positionX>position.positionX) return 1;
        }
        return -1;
    }

    public double distance(Position pos) {
        double y = Math.pow(this.positionY - pos.positionY, 2);
        double x = Math.pow(this.positionX - pos.positionX, 2);
        return Math.sqrt(x+y);
    }

    public int getX() {
        return positionX;
    }

    public void setX(int positionX) {
        this.positionX = positionX;
    }

    public int getY() {
        return positionY;
    }

    public void setY(int positionY) {
        this.positionY = positionY;
    }

    public Position moveRight() { return at(positionX+1, positionY);}

    public Position moveLeft() { return at(positionX-1, positionY);}

    public Position moveUp() { return at(positionX, positionY-1);}

    public Position moveDown() { return at(positionX, positionY+1);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return (positionX == position.getX()) && (positionY == position.getY());
    }

}
