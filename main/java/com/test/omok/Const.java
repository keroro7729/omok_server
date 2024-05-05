package com.test.omok;

public class Const {
    public final static int DRAW_THRESHOLD = 170;
    public final static int BLACK = 1, WHITE = 2, BLACK_WIN = 3, WHITE_WIN = 4, DRAW = 5;
    public final static int SPHB = -1, FFPHB = -2, TTPHB = -3;
    public final static int[] DIRX = {-1, -1, 0, 1, 1, 1, 0, -1}, DIRY = {0, 1, 1, 1, 0, -1, -1, -1};
    public final static int[] FIVE = {-1, 1, 1, 1, 1, 1, -1}, OPEN_FOUR = {-1, 0, 1, 1, 1, 1, 0, -1};
    public final static int[][] FFEXCEPTIONS = {{-1, 1, 1, 1, 0, 0, 0, 1, 1, 1, -1}, {-1, 1, 0, 1, 0, 1, 0, 1, -1}};
}
