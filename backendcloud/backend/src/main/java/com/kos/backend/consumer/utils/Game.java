package com.kos.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kos.backend.consumer.WebSocketServer;
import com.kos.backend.pojo.Bot;
import com.kos.backend.pojo.Record;
import com.kos.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {

    private final Integer cols;
    private final Integer rows;
    private int[][] g;
    private int extendSize;
    private int wallsRandomNum;
    private final int innerWallsNum;
    private final Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";
    private String loser = "";
    private boolean firstRound = true;
    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    public Integer getCols() {
        return cols;
    }
    public Integer getRows() {
        return rows;
    }
    public Game(
            Integer rows,
            Integer cols,
            Integer idA,
            Bot botA,
            Integer idB,
            Bot botB
    ) {
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
        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }
        playerA = new Player(idA, botIdA, botCodeA, this.rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, botIdB, botCodeB, 1, this.cols - 2, new ArrayList<>());
    }
    public Player getPlayerA() {
        return playerA;
    }
    public Player getPlayerB() {
        return playerB;
    }
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }

    }
    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
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

    private String getInput(Player player) {
        Player me, you;
        if (playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }
        return getMapString() + "#" +
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStepsString() + ")#" +
                you.getSx() + "#" +
                you.getSy() + "#(" +
                you.getStepsString() + ")";
    }
    private void sendBotCode(Player player) {
        if (player.getBotId().equals(-1)) return;
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));

        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }
    private boolean nextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendBotCode(playerA);
        sendBotCode(playerB);
        for (int i = 0; i < 50; ++i) {
            try {
                Thread.sleep(200);
                JSONObject resp = new JSONObject();
                resp.put("time", 10 - (i * 0.2));
                sendAllMessage(resp.toJSONString());
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (g[cell.x][cell.y] == 1) return false;
        for (int i = 0; i < n - 1; ++i) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) return false;
        }

        for (int i = 0; i < n - 1; ++i) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) return false;
        }
        return true;


    }
    private void judge() {
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) loser = "all";
            else if (!validA) loser = "A";
            else loser = "B";
        }
    }

    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null) WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null) WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }
    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }
    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {
                res.append(this.g[i][j]);
            }
        }
        return res.toString();
    }
    private void updataUserRating(Player player, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    private void updataUserRank(Player player, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        String rank = "";
        if (rating < 0) rank = "屡败屡战";
        else if (rating <= 10) rank = "菜鸟";
        else if (rating <= 50) rank = "青铜";
        else if (rating <= 100) rank = "白银";
        else if (rating <= 200) rank = "黄金";
        else if (rating <= 500) rank = "铂金";
        else if (rating <= 1000) rank = "钻石";
        else if (rating <= 2000) rank = "大师";
        else rank = "霸主";
        user.setTierName(rank);
        WebSocketServer.userMapper.updateById(user);
    }
    private void saveToDatabase() {
        Integer ratingA = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        if ("A".equals(loser)) {
            ratingA -= 2;
            ratingB += 5;
        } else if ("B".equals(loser)) {
            ratingA += 5;
            ratingB -= 2;
        }

        updataUserRating(playerA, ratingA);
        updataUserRating(playerB, ratingB);
        updataUserRank(playerA, ratingA);
        updataUserRank(playerB, ratingB);

        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                this.rows,
                this.cols,
                this.loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }
    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }
    @Override
    public void run() {
        for (int i = 0; i < 5000; ++i) {
            if (firstRound)
            {
                firstRound = false;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (nextStep()) {
                judge();
                if (status.equals("playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                if (!loser.equals("all") && !loser.equals("A") && !loser.equals("B")) {

                    status = "finished";
                    lock.lock();
                    try {
                        if (nextStepA == null && nextStepB == null) {
                            loser = "all";
                        } else if (nextStepA == null) {
                            loser = "A";
                        } else {
                            loser = "B";
                        }
                    } finally {
                        lock.unlock();
                    }
                    sendResult();
                    break;
                }
            }
        }
    }
}
