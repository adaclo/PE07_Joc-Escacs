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
        Boolean playAgain=false;

        initializeBoard(board);
        choosePlayer(s, players);
        randomOrder(players);
        showBoard(board);
        do {
            do {
                for (int p=0;p<players.length;p++) {
                    newTurn(p,players,s,board);
                }
            } while (!finished);
            Boolean validOpt=false;
            while(!validOpt) {
                System.out.print(YELLOW+"\nDo you wanna play again with same players? (Y/N) "+RESET);
                String r = s.next();
            }
        } while (playAgain);
    }

    public void readPosition(int p, Scanner s, String text,int[] position,char o_d,Boolean[]validMovement) {
        Boolean validOpt=false;
        String temp;
        while (!validOpt) {
            System.out.printf(YELLOW+text+RESET);
            temp = s.next();
            if (o_d=='d'&&temp.equalsIgnoreCase("X")) {
                validMovement[0]=false;
                validMovement[1]=true;
                validOpt=true;
                System.out.println(GREEN+"You successfully unselected the first piece!"+RESET);
            } else {
                if (temp.length()==2) {
                    try {
                        char charPos = temp.charAt(0);
                        char numPos = temp.charAt(1);
                        if (Character.isLetter(charPos)) {
                            charPos = Character.toLowerCase(charPos);
                        }
                        if ((charPos>= 'a' && charPos <= 'h')&&(numPos>='1' && numPos<='8')) {
                            int posX = charPos - 'a';
                            int posY = Character.getNumericValue(numPos); // ES -1 POQUE LA ARRAY EMPIEZA EN 0
                            posY = Math.abs(posY-8);
                            validOpt=true;
                            if(o_d=='o') {
                                position[0]=posX;
                                position[1]=posY;
                            } else if (o_d=='d') {
                                position[2]=posX;
                                position[3]=posY;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }

    public void newTurn(int p, String[] players, Scanner s,char[][]board) {
        
        if (p==0) { // SI es el jugador BLANCO
            System.out.printf("\nIt's turn of %s%s%s: ",BOLD,players[p],RESET);
        } else if (p==1) { // SI es el jugador ROJO
            System.out.printf("\nIt's turn of %s%s%s%s: ",RED,BOLD,players[p],RESET);
        }
        Boolean[] validMovement = {false,false};
        int[] movement = new int[4];
        do {
            validMovement[0]=false;
            do {
                validMovement[1]=false;
                readPosition(p,s, "\nPlease enter the position of the piece you wanna move: ",movement,'o',validMovement);
                validOrigin(p,board,validMovement,movement);
            } while (!validMovement[0]);
            do {
                readPosition(p,s, "\nPlease enter the position of the destionation ('X' to cancel first piece): ",movement,'d',validMovement);
                if (validMovement[0]) {
                    validDestination(p,board,validMovement,movement);
                }
            } while (!validMovement[1]);
        } while (!validMovement[0]);
        showBoard(board);
    }

    public void validDestination(int p,char[][]board,Boolean[]validMovement,int[]movement) {
        int posXorg=movement[0];
        int posYorg=movement[1];
        int posXdest=movement[2];
        int posYdest=movement[3];
        char orgPiece = board[posYorg][posXorg];

        orgPiece = Character.toLowerCase(orgPiece);

        System.out.println(orgPiece);

        switch (orgPiece) {
            case 'p': // PAWNS
                validPawnMove(p,board, movement, posXorg, posYorg, posXdest, posYdest,validMovement);
                break;
            case 't': // TOWERS
                
                break;
            case 'h': // HORSES
                
                break;
            case 'b': // BISHOPS
                
                break;
            case 'k': // KINGS
                
                break;
            case 'q': // QUEENS
                
                break;
        
            default:
                break;
        }
    }

    public void validPawnMove(int p,char[][]board,int[]movement,int orgX,int orgY, int dstX, int dstY,Boolean[]validMovement) {

        int startPos,move;
        System.out.printf("ORIGEN %d%d",orgX,orgY);
        System.out.printf("DEST %d%d",dstX,dstY);

        if(p==0) {
            startPos=6;
            move=-1;
        } else {
            startPos=1;
            move=1;
        }

        char destField = board[dstY][dstX];
        if (orgX==dstX&& // Si va recto
            destField==' '&& // Si el destino esta vacio
            dstY==orgY+move // Si se mueve 1 casilla
        ){
            board[dstY][dstX]=board[orgY][orgX];
            board[orgY][orgX]=' ';
            validMovement[1]=true;
        }
    }

    public void validOrigin(int p, char[][]board, Boolean[] validMovement, int[] movement) {
        int posX=movement[0];
        int posY=movement[1];
        if(p==0&&Character.isUpperCase(board[posY][posX])) { // Es el turno del blanco y selecciona una de sus piezas
            validMovement[0]=true;
            movement[0] = posX;
            movement[1] = posY;
        } else if (p==1&&Character.isLowerCase(board[posY][posX])) { // Es el turno del rojo y selecciona una de sus piezas
            validMovement[0]=true;
            movement[0] = posX;
            movement[1] = posY;
        } else if ((p==0&&Character.isLowerCase(board[posY][posX]))||(p==1&&Character.isUpperCase(board[posY][posX]))) {
            System.out.println(RED+"(!) This is not your piece."+RESET);
        } else {
            System.out.println(RED+"(!) You didn't selected any piece."+RESET);
        }
    }

    public void randomOrder(String[] players) { // Método para elegir aleatoriamente los colors
        // 0 == BLANCO
        // 1 == ROJO 
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
                if (f==0||f==1) { // RED PIECES
                    if (f==1) { // Red Pawns
                        board[f][c] = 'p';
                    } else if (f==0&&(c==0||c==7)) { // Red Towers
                        board[f][c] = 't';
                    } else if (f==0&&(c==1||c==6)) { // Red Horse
                        board[f][c] = 'h';
                    } else if (f==0&&(c==2||c==5)) { // Red Bishop
                        board[f][c] = 'b';
                    } else if (f==0&&c==3) { // Red Queen
                        board[f][c] = 'q';
                    } else if (f==0&&c==4) { // Red King
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
                System.out.printf("  | %sA%s | %sB%s | %sC%s | %sD%s | %sE%s | %sF%s | %sG%s | %sH%s |  ",GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET);
                System.out.print("\n------------------------------------");
                System.out.println();
            }
            for (int c=0;c<board[0].length;c++) {
                if (c==0) {
                    System.out.print(GREEN+Math.abs(f-board[0].length)+RESET+" ");
                }
                if (Character.isLowerCase(board[f][c])) {
                    System.out.print("| " + RED+board[f][c]+RESET + " ");
                } else {
                    System.out.print("| " + board[f][c] + " ");
                }
                if (c==board[0].length-1) {
                    System.out.print("| "+GREEN+Math.abs(f-board[0].length)+RESET);
                }
            }
            System.out.println();
            System.out.print("------------------------------------");
            if(f==7) {
                System.out.printf("\n  | %sA%s | %sB%s | %sC%s | %sD%s | %sE%s | %sF%s | %sG%s | %sH%s |  ",GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET,GREEN,RESET);
            }
            System.out.println();
        }
    }
}
