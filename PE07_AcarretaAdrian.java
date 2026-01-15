package Activitats.PE07;

import java.util.ArrayList;
import java.util.Scanner;

public class PE07_AcarretaAdrian {
    public static void main(String[] args) {
        PE07_AcarretaAdrian p = new PE07_AcarretaAdrian();
        p.principal();
    }

    // COLORS
    final String RESET = "\u001B[0m";
    final String RED = "\u001B[31m";
    final String GREEN = "\u001B[32m";
    final String YELLOW = "\u001B[33m";
    final String BLUE = "\u001B[34m";
    final String BOLD = "\u001B[1m";
    final String BOLDoff = "\u001B[0m";

    public void principal() {
        String r;
        Scanner s = new Scanner(System.in);

        Boolean validOpt = false;
        do {
            System.out.print(YELLOW+"\nWould you like to start a new Chess Game? (Y/N) "+RESET);
            r = s.next();

            if (r.equalsIgnoreCase("y")) {
                startGame(s);
                validOpt=true;
            } else if (r.equalsIgnoreCase("n")) {
                System.out.println(RED+"\nExiting program..."+RESET);
                validOpt=true;
            }
        } while (!validOpt);
    }

    public void startGame(Scanner s) {
        char[][] board = new char[8][8];
        char[][] movingBoard = new char[8][8];
        String[] players = new String[2];
        ArrayList<String> movements = new ArrayList<String>();
        ArrayList<String> deadPieces = new ArrayList<String>();
        Boolean finished=false;

        initializeBoard(board);
        choosePlayer(s, players);
        randomOrder(players);
        showBoard(board);
        do {
            for (int p=0;p<players.length;p++) {
                newTurn(p,players);
            }
        } while (!finished);
    }

    public void newTurn(int p, String[] players) {
        
        if (p==0) {
            System.out.printf("\nIt's turn of %s%s%s: ",BOLD,players[p],RESET);
        } else if (p==1) {
            System.out.printf("\nIt's turn of %s%s%s%s: ",RED,BOLD,players[p],RESET);
        }
        
    }

    public void randomOrder(String[] players) { // Método para elegir aleatoriamente los colors
        // 0 == BLANCO
        // 1 == NEGRO 
        int randomNum = (int)(Math.random()*2);
        String temp;
        if (randomNum==1) { // Alterna los órdenes si cae en 1
            temp = players[0];
            players[0]=players[1];
            players[1]=temp;
        } // No hay else porque si cae en 0 ya está bien ordenado
        System.out.printf("\n%s%s %s%splaying with %s%swhite%s %spieces\n%s%s%s %s%splaying with %s%sred%s %spieces%s",BOLD,players[0],RESET,YELLOW,RESET,BOLD,RESET,YELLOW,RED,BOLD,players[1],RESET,YELLOW,BOLD,RED,RESET,YELLOW,RESET);
    }

    public void choosePlayer(Scanner s, String[] players) {
        Boolean finished=false;
        while (!finished) {
            s.nextLine(); // Clean buffer
            for (int p=0;p<players.length;p++) {
                System.out.print(YELLOW+"\n(#) Please enter name of player ("+(p+1)+") "+RESET);
                players[p]=s.nextLine();
            }
            Boolean validOpt=false;
            do {
                System.out.print(YELLOW+"\nAre you sure '"+players[0]+"'"+" and '"+players[1]+"'"+" will be the players? (Y/N) "+RESET);
                String r = s.next();
                if (r.equalsIgnoreCase("y")) {
                    finished=true;
                    validOpt=true;
                } else if (r.equalsIgnoreCase("n")) {
                    validOpt=true;
                }
            } while (!validOpt);
        }
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
                if (Character.isLowerCase(board[f][c])) {
                    System.out.print("| " + RED+board[f][c]+RESET + " ");
                } else {
                    System.out.print("| " + board[f][c] + " ");
                }
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
