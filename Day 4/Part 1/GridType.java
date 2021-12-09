/**
 * New type containing int to store the value of number and flag if the number has been checked.
 */
class GridType {
    int value;
    boolean isChecked = false;

    GridType() {
        this.value = -1;
    }

    GridType(int num) {
        this.value = num;
    }

    //Copy constructor to perform deep copying
    GridType(GridType object) {
        this.value = object.value;
        this.isChecked = object.isChecked;
    }
}
