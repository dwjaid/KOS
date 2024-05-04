package com.kos.backend.consumer.utils;

import java.util.Random;

public class Game {

    final private Integer cols;
    final private Integer rows;
    private int[][] g;
    private int extendSize;
    private int wallsRandomNum;
    final private int innerWallsNum;

    public Integer getCols() {
        return cols;
    }
    public Integer getRows() {
        return rows;
    }
    public Game(Integer rows, Integer cols) {
        Random random = new Random();
        this.extendSize = random.nextInt(20);
        if (this.extendSize % 2 == 0) {
            this.extendSize = this.extendSize - 1;
        }

        this.cols = rows + this.extendSize;
        this.rows = cols + this.extendSize;
        this.g = new int [this.rows][this.cols];
        this.wallsRandomNum = 6;
        int innerWallsNum1 = random.nextInt((this.rows * this.cols) / this.wallsRandomNum);
        if (innerWallsNum1 <= this.rows) {
            innerWallsNum1 += this.rows;
        }
        this.innerWallsNum = innerWallsNum1;
    }
    public boolean checkConnectivity(int[][] g, int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) {
            return true;
        }
        g[sx][sy] = 1;

        int[] dx = { -1, 0, 1, 0 };
        int[] dy = { 0, 1, 0, -1 };
        for (int i = 0; i < 4; ++i) {
            int x = sx + dx[i];
            int y = sy + dy[i];
            if (g[x][y] == 0 && checkConnectivity(g, x, y, tx, ty)) {
                return true;
            }
        }
        return false;
    }
    public boolean createWalls() {
        int[][] g = new int[this.rows][this.cols];
        for (int r = 0; r < this.rows; ++r) {
            for (int c = 0; c < this.cols; ++c) {
                g[r][c] = 0;
            }
        }
        for (int r = 0; r < this.rows; ++r) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }

        for (int c = 0; c < this.cols; ++c) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.innerWallsNum / 2; ++i) {
            for (int j = 0; j < 1000; ++j) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) {
                    continue;
                }
                if ((r == this.rows - 2 && c == 1) || (r == 1 && c == this.cols - 2)) {
                    continue;
                }
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        int[][] copyG = new int[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(g[i], 0, copyG[i], 0, this.cols);
        }

        if (!checkConnectivity(copyG, this.rows - 2, 1, 1, this.cols - 2)) {
            return false;
        }
        this.g = g;
        return true;
    }

    public int[][] getG() {
        return g;
    }

    public void createMap() {
        for (int i = 0; i < 1000; ++i) {
            if (createWalls()) break;
        }
    }
}
