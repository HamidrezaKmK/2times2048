package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.xml.soap.Node;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Block {
    private int value;


    Block(int value) {
        this.value = value;
    }

    Block(Block block) {
        this.value = block.value;
    }

    public int getValue() {
        return value;
    }
}
