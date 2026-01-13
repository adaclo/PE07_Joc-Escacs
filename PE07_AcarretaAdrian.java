package Activitats.PE07;

import java.util.Scanner;

public class PE07_AcarretaAdrian {
    public static void main(String[] args) {
        PE07_AcarretaAdrian p = new PE07_AcarretaAdrian();
        p.principal();
    }

    public void principal() {
        // COLORS
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String BLUE = "\u001B[34m";
        String r;
        Scanner s = new Scanner(System.in);

        Boolean validOpt = false;
        do {
            System.out.println(YELLOW+"Would you like to start a new Chess Game? (Yes/No)"+RESET);
            r = s.next();

            if (r.equalsIgnoreCase("yes")) {
                startGame();
                validOpt=true;
            } else if (r.equalsIgnoreCase("no")) {
                System.out.println(RED+"\nExiting program..."+RESET);
                validOpt=true;
            }
        } while (!validOpt);
    }

    public void startGame() {
        char[][] board = new char[8][8];
        char[][] movingBoard = new char[8][8];

        initializeBoard(board);
        showBoard(board);
    }

    public void initializeBoard(char[][] board) {
        for (int f=0;f<board.length;f++) {
            for (int c=0;c<board.length;c++) {
                board[f][c] = '*';
            }
        }
    }

    public void showBoard(char[][] board) {
        for (int f=0;f<board.length;f++) {
            if (f==0) {
                System.out.println();
                System.out.print("---------------------------------");
                System.out.println();
            }
            for (int c=0;c<board[0].length;c++) {
                System.out.print("| " + board[f][c] + " ");
                if (c==board[0].length-1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            System.out.print("---------------------------------");
            System.out.println();
        }
    }
}
