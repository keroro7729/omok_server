package com.test.omok;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class OmokEngine {

    protected int[][] board;
    protected List<Integer> gameLog;
    protected int gameState;

    private void init(){
        board = new int[15][15];
        gameLog = new ArrayList<>();
        gameState = Const.BLACK;
        put(7, 7);
    }
    public OmokEngine(){
        init();
    }

    public void resetBy(List<Integer> gameLog){
        this.gameLog = gameLog;
        board = new int[15][15];
        int count = 1;
        for(int coor : gameLog){
            setBoard(coor, count++);
        }
        updateBoard();
        int lastColor = gameLog.size()%2 == 1 ? Const.BLACK : Const.WHITE;
        if(isEnd(gameLog.get(gameLog.size()-1), lastColor) != -1){
            gameState = lastColor == Const.BLACK ? Const.BLACK_WIN : Const.WHITE_WIN;
        }
        else if(gameLog.size() >= Const.DRAW_THRESHOLD) gameState = Const.DRAW;
        else gameState = lastColor == Const.BLACK ? Const.WHITE : Const.BLACK;
    }

    public boolean put(int coor){
        return put(getX(coor), getY(coor));
    }
    public boolean put(int x, int y){
        if(gameState != Const.BLACK && gameState != Const.WHITE) {
            return false;
        }
        if(x < 0 || 15 <= x || y < 0 || 15 <= y)
            return false;
        if(board[x][y] > 0 || (gameState == Const.BLACK && board[x][y] < 0))
            return false;
        gameLog.add(getCoor(x, y));
        board[x][y] = gameLog.size();

        int endDir = isEnd(x, y, getColor(x, y));
        if(endDir != -1){
            if(gameState == Const.BLACK)
                gameState = Const.BLACK_WIN;
            else if(gameState == Const.WHITE)
                gameState = Const.WHITE_WIN;
        }
        else{
            if(gameLog.size() > Const.DRAW_THRESHOLD){
                gameState = Const.DRAW;
            }
            else if(gameState == Const.BLACK)
                gameState = Const.WHITE;
            else if(gameState == Const.WHITE)
                gameState = Const.BLACK;
        }
        updateBoard(x, y);
        return true;
    }

    protected void updateBoard(int X, int Y){
        List<Integer> list = new ArrayList<>();
        for(int x=X-4; x<=X+4; x++){
            for(int y=Y-4; y<=Y+4; y++){
                if(indexOut(x, y)) continue;
                if(board[x][y] > 0) continue;
                if(isSPHB(x, y)) board[x][y] = Const.SPHB;
                else{
                    board[x][y] = 0;
                    list.add(getCoor(x, y));
                }
            }
        }
        updateTTnFFPHB(list);
        for(int x=X-4; x<=X+4; x++){
            for(int y=Y-4; y<=Y+4; y++) {
                if(indexOut(x, y)) continue;
                if(board[x][y] < 0 && isEnd(x, y, Const.BLACK) != -1)
                    board[x][y] = 0;
            }
        }
    }
    protected void updateBoard(){
        List<Integer> list = new ArrayList<>();
        for(int coor=0; coor<225; coor++){
            if(getBoard(coor) > 0) continue;
            if(isSPHB(coor)) setBoard(coor, Const.SPHB);
            else{
                setBoard(coor, 0);
                list.add(coor);
            }
        }
        updateTTnFFPHB(list);
        for(int coor=0; coor<225; coor++){
            if(getBoard(coor) < 0 && isEnd(coor, Const.BLACK) != -1)
                setBoard(coor, 0);
        }
    }

    protected void updateTTnFFPHB(List<Integer> list){
        List<Integer> tmp = new ArrayList<>();
        for(int i=list.size()-1; i>=0; i--){
            if(isFFPHB(list.get(i))){
                tmp.add(list.get(i));
                list.remove(i);
            }
        }
        for(int coor : tmp) setBoard(coor, Const.FFPHB);
        tmp.clear();

        for(int i=list.size()-1; i>=0; i--){
            if(isTTPHB(list.get(i))){
                tmp.add(list.get(i));
            }
        }
        for(int coor: tmp) setBoard(coor, Const.TTPHB);
    }

    protected int almostMatch(int X, int Y, int dir, int color, int[] pattern){
        int coor = -1;
        for(int i=0; i<pattern.length; i++){
            int x = X + Const.DIRX[dir] * i, y = Y + Const.DIRY[dir] * i;
            if(pattern[i] == -1){
                if(indexOut(x, y)) continue;
                if(color == Const.BLACK && isBlack(x, y)) return -1;
            }
            else if(pattern[i] == 0){
                if(indexOut(x, y)) return -1;
                if(board[x][y] > 0 || (color == Const.BLACK && board[x][y] < 0)) return -1;
            }
            else if(pattern[i] == 1){
                if(indexOut(x, y)) return -1;
                if(getColor(x,y) != color){
                    if(board[x][y] > 0 || (color == Const.BLACK && board[x][y] < 0)) return -1;
                    if(coor == -1) coor = getCoor(x, y);
                    else return -1;
                }
            }
        }
        return coor;
    }

    protected boolean perfectMatch(int X, int Y, int dir, int color, int[] pattern){
        for(int i=0; i<pattern.length; i++){
            int x = X + Const.DIRX[dir] * i, y = Y + Const.DIRY[dir] * i;
            if(pattern[i] == -1){
                if(indexOut(x, y)) continue;
                if(color == Const.BLACK && isBlack(x, y)) return false;
            }
            else if(pattern[i] == 0){
                if(indexOut(x, y)) return false;
                if(board[x][y] > 0 || (color == Const.BLACK && board[x][y] < 0)) return false;
            }
            else if(pattern[i] == 1){
                if(indexOut(x, y)) return false;
                if(getColor(x,y) != color) return false;
            }
        }
        return true;
    }

    protected boolean isSPHB(int coor){
        return isSPHB(getX(coor), getY(coor));
    }
    protected boolean isSPHB(int X, int Y){
        for(int dir=0; dir<4; dir++){
            int count = 0;
            for(int i=1; i<=4; i++){
                int x = X + Const.DIRX[dir] * i, y = Y + Const.DIRY[dir] * i;
                if(indexOut(x, y) || !isBlack(x, y)) break;
                count++;
            }
            if(count == 0) continue;
            for(int i=1; i<=4; i++){
                int x = X - Const.DIRX[dir] * i, y = Y - Const.DIRY[dir] * i;
                if(indexOut(x, y) || !isBlack(x, y)) break;
                count++;
            }
            if(count >= 5) return true;
        }
        return false;
    }

    protected boolean isFFPHB(int coor){
        return isFFPHB(getX(coor), getY(coor));
    }
    protected boolean isFFPHB(int X, int Y){
        for(int dir=0; dir<4; dir++){
            for(int[] pattern : Const.FFEXCEPTIONS) {
                int x = X - Const.DIRX[dir] * (pattern.length/2), y = Y - Const.DIRY[dir] * (pattern.length/2);
                if(perfectMatch(x, y, dir, Const.BLACK, pattern)) return true;
            }
        }
        board[X][Y] = 1;
        int count = 0;
        for(int dir=0; dir<4; dir++){
            for(int i=1-Const.FIVE.length; i<=0; i++){
                int x = X + Const.DIRX[dir] * i, y = Y + Const.DIRY[dir] * i;
                if(almostMatch(x, y, dir, Const.BLACK, Const.FIVE) != -1){
                    count++;
                    break;
                }
            }
        }
        board[X][Y] = 0;
        if(count >= 2) return true;
        return false;
    }

    protected boolean isTTPHB(int coor){
        return isTTPHB(getX(coor), getY(coor));
    }
    protected boolean isTTPHB(int X, int Y){
        board[X][Y] = 1;
        int count = 0;
        for(int dir=0; dir<4; dir++){
            for(int i=1-Const.OPEN_FOUR.length; i<=0; i++){
                int x = X + Const.DIRX[dir] * i, y = Y + Const.DIRY[dir] * i;
                int coor = almostMatch(x, y, dir, Const.BLACK, Const.OPEN_FOUR);
                if(coor == -1) continue;

                // check false three
                if(!isFFPHB(coor)){
                    count++;
                    break;
                }
                int dx = getX(coor), dy = getY(coor);
                boolean right = isBlack(dx+Const.DIRX[dir], dy+Const.DIRY[dir]);
                boolean left = isBlack(dx-Const.DIRX[dir], dy-Const.DIRY[dir]);
                if(!left && right){
                    dx += Const.DIRX[dir] * 5;
                    dy += Const.DIRY[dir] * 5;
                    if(indexOut(dx, dy) || board[dx][dy] != 0 ) continue;
                    if(isFFPHB(dx-Const.DIRX[dir], dy-Const.DIRY[dir])) continue;
                    count++;
                    break;
                }else if(left && !right){
                    dx -= Const.DIRX[dir] * 5;
                    dy -= Const.DIRY[dir] * 5;
                    if(indexOut(dx, dy) || board[dx][dy] != 0 ) continue;
                    if(isFFPHB(dx+Const.DIRX[dir], dy+Const.DIRY[dir])) continue;
                    count++;
                    break;
                }
            }
        }
        board[X][Y] = 0;
        if(count >= 2) return true;
        return false;
    }

    protected int isEnd(int coor, int color){
        return isEnd(getX(coor), getY(coor), color);
    }
    protected int isEnd(int X, int Y, int color){
        for(int dir=0; dir<4; dir++){
            int count = 0;
            for(int i=1; i<=4; i++){
                int x = X + Const.DIRX[dir] * i, y = Y + Const.DIRY[dir] * i;
                if(indexOut(x, y) || getColor(x, y) != color) break;
                count++;
            }
            for(int i=1; i<=4; i++){
                int x = X - Const.DIRX[dir] * i, y = Y - Const.DIRY[dir] * i;
                if(indexOut(x, y) || getColor(x, y) != color) break;
                count++;
            }
            if(count == 4 || (color == Const.WHITE && count > 4)) return dir;
        }
        return -1;
    }

    protected boolean isBlack(int coor){ return isBlack(getX(coor), getY(coor)); }
    protected boolean isBlack(int x, int y){ return (board[x][y] > 0 && board[x][y]%2 == 1); }
    protected int getColor(int coor){ return getColor(getX(coor), getY(coor)); }
    protected int getColor(int x, int y){
        if(board[x][y] > 0){
            if(board[x][y]%2 == 1) return Const.BLACK;
            else return Const.WHITE;
        }
        else return board[x][y];
    }
    protected boolean indexOut(int coor){ return indexOut(getX(coor), getY(coor)); }
    protected boolean indexOut(int x, int y){ return (x < 0 || 15 <= x || y < 0 || 15 <= y); }
    protected void setBoard(int coor, int val){ board[getX(coor)][getY(coor)] = val; }
    protected int getBoard(int coor){ return board[getX(coor)][getY(coor)]; }
    protected int getX(int coor){ return coor/15; }
    protected int getY(int coor){ return coor%15; }
    protected int getCoor(int x, int y){ return x*15+y; }
}

