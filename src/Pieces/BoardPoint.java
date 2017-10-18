package Pieces;

public class BoardPoint {
    public int x;
    public int y;

    public BoardPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + " " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        BoardPoint point = (BoardPoint)obj;
        //System.out.println("Equals X: " + this.x + " " + point.x);
        //System.out.println("Equals Y: " + this.y + " " + point.y);
        return this.x == point.x && this.y == point.y;
    }
}
