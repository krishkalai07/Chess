public class BoardPoint {
    public int x;
    public int y;

    /**
     * Constructor for a point.
     * @param x x coordinate
     * @param y y coordinate
     */
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
        return this.x == point.x && this.y == point.y;
    }
}
