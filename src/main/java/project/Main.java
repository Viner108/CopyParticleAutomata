package project;

import oneOval.MoveOneOval;

public class Main {
    public static void main(String[] args) {
        Form f = new Form();
        MoveOneOval m=new MoveOneOval();
        new Thread(m).start();
    }
}
