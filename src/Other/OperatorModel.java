package Other;

public class OperatorModel {
    public int[][] modelValue;
    public int rows;
    public int columns;

    public OperatorModel(int r, int c, int[] values){
        modelValue = new int[r][c];
        for (int i = 0; i < r; i++) {
            if (c >= 0)
                System.arraycopy(values, i * c, modelValue[i], 0, c);
        }
        rows = r;
        columns = c;
    }
}
