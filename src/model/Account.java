package model;

import com.gilecode.yagson.YaGson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Account implements Comparable<Account> {
    String username;
    String password;
    int highScore = 0;
    private static ArrayList<Account> accounts = new ArrayList<>();

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        accounts.add(this);
    }

    public Account(Account account) {
        this.username = account.username;
        this.password = account.password;
        this.highScore = account.highScore;
    }

    public void setScore(int score) {
        highScore = Math.max(score, highScore);
    }

    public int getHighScore() {
        return highScore;
    }

    public String getPassword() {
        return password;
    }

    public static ArrayList<Account> getScoreBoard() {
        ArrayList<Account> scoreBoard = new ArrayList<>();
        try {
            for (File file : new File("./accounts").listFiles()) {
                YaGson yaGson = new YaGson();
                scoreBoard.add(new Account(yaGson.fromJson(new BufferedReader(new FileReader(file)), Account.class)));
            }
        } catch (IOException ignore) {
            System.err.println("NO accounts file created!");
        }
        Collections.sort(scoreBoard);
        return scoreBoard;
    }

    @Override
    public int compareTo(Account account) {
        return account.highScore - this.highScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }
}
