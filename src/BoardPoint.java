public class BoardPoint {
    int x;
    int y;

    public BoardPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + " " + y + ")";
    }
}