class GridType {
    char value;
    boolean isChecked = false;
    int x;
    int y;

    GridType() {}

    GridType(char value, int y, int x) {
        this.value = value;
        this.y = y;
        this.x = x;
    }
}