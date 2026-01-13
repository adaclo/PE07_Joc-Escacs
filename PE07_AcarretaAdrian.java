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
                if (f==0||f==1) { // BLACK PIECES
                    if (f==1) { // Black Pawns
                        board[f][c] = 'p';
                    } else if (f==0&&(c==0||c==7)) { // Black Towers
                        board[f][c] = 't';
                    } else if (f==0&&(c==1||c==6)) { // Black Horse
                        board[f][c] = 'h';
                    } else if (f==0&&(c==2||c==5)) { // Black Bishop
                        board[f][c] = 'b';
                    } else if (f==0&&c==3) { // Black Queen
                        board[f][c] = 'q';
                    } else if (f==0&&c==4) { // Black King
                        board[f][c] = 'k';
                    }
                } else if (f==6||f==7) { // WHITE PIECES
                    if (f==6) { // White Pawns
                        board[f][c] = 'P';
                    } else if (f==7&&(c==0||c==7)) { // White Towers
                        board[f][c] = 'T';
                    } else if (f==7&&(c==1||c==6)) { // White Horse
                        board[f][c] = 'H';
                    } else if (f==7&&(c==2||c==5)) { // White Bishop
                        board[f][c] = 'B';
                    } else if (f==7&&c==3) { // White Queen
                        board[f][c] = 'Q';
                    } else if (f==7&&c==4) { // White King
                        board[f][c] = 'K';
                    }
                } else {
                    board[f][c] = ' '; // NO PIECE
                }
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
