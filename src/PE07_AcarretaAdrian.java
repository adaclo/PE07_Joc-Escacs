package Activitats.PE07.src;

import java.util.ArrayList;
import java.util.InputMismatchException;
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
        String[] players = new String[2];
        int[] gamesWon = {0,0};
        ArrayList<String> movements = new ArrayList<String>();
        ArrayList<Character> deadPieces = new ArrayList<Character>();
        ArrayList<Character> promotedPieces = new ArrayList<Character>();
        Boolean finished=false;
        Boolean playAgain=false;

        initializeBoard(board);
        choosePlayer(s, players);
        randomOrder(players);
        showBoard(board);
        do {
            int p0=gamesWon[0]; // para comprobar si se le ha sumado una victoria
            int winner;
            do {
                for (int p=0;p<players.length;p++) {
                    finished=newTurn(p,players,s,board,movements,deadPieces,gamesWon,promotedPieces);
                    if (finished){
                        p=2; // parar for
                    }
                }
            } while (!finished);
            if(p0!=gamesWon[0]) { // descubrir quien es el ganador
                winner=0;
            } else {
                winner=1;
            }
            if(winner==0) {
                System.out.printf("\n%s%s%s%s has won the game!%s",BOLD,players[0],RESET,GREEN,RESET);
            } else {
                System.out.printf("\n%s%s%s%s%s has won the game!%s",RED,BOLD,players[1],RESET,GREEN,RESET);
            }
            System.out.print("\nThese are all the movements on the match:\n");
            for (int m=0;m<movements.size();m++) {
                if((m/2)%2==0) {
                    System.out.print(BOLD+movements.get(m)+BOLDoff+", ");
                } else {
                    System.out.print(RED+BOLD+movements.get(m)+BOLDoff+RESET+", ");
                }
            }
            if (deadPieces.size()!=0) {
                System.out.print("\nThese are all the dead pieces in the match:\n");
                for (int p=0;p<deadPieces.size();p++) {
                    if (Character.isUpperCase(deadPieces.get(p))) {
                        System.out.print(BOLD+deadPieces.get(p)+BOLDoff+", ");
                    } else {
                        System.out.print(RED+BOLD+deadPieces.get(p)+BOLDoff+RESET+", ");
                    }
                }
            }
            if (promotedPieces.size()!=0) {
                System.out.print("\nThese are all the promoted pieces in the match:\n");
                for (int p=0;p<promotedPieces.size();p++) {
                    if (Character.isUpperCase(promotedPieces.get(p))) {
                        System.out.print(BOLD+promotedPieces.get(p)+BOLDoff+", ");
                    } else {
                        System.out.print(RED+BOLD+promotedPieces.get(p)+BOLDoff+RESET+", ");
                    }
                }
            }
            System.out.println();
            Boolean validOpt=false;
            while(!validOpt) {
                System.out.print(YELLOW+"\nDo you wanna play again with same players? (Y/N) "+RESET);
                String r = s.next();
                if (r.equalsIgnoreCase("y")) {
                    playAgain=true;
                    validOpt=true;
                    if (winner==1) {
                        String temp=players[0];
                        players[0]=players[1]; // cambio el jugador rojo (ganador) al blanco
                        players[1]=temp;
                        int tempInt=gamesWon[0]; // igual con las partidas ganadas
                        gamesWon[0]=gamesWon[1];
                        gamesWon[1]=tempInt;
                    }
                    finished=false;
                    initializeBoard(board);
                    movements.clear();
                    deadPieces.clear();
                    showBoard(board);
                } else if (r.equalsIgnoreCase("n")) {
                    playAgain=false;
                    System.out.println(GREEN+BOLD+"GAME STATS"+RESET);
                    System.out.printf("\n%s%s%s has won a total of %s%s%s matches",BOLD,players[0],BOLDoff,BOLD,gamesWon[0],RESET);
                    System.out.printf("\n%s%s%s%s has won a total of %s%s%s%s matches",RED,BOLD,players[1],RESET,RED,BOLD,gamesWon[1],RESET);
                }
                System.err.println();
                validOpt=true;
            }
        } while (playAgain);
    }

    public Boolean readPosition(int p, Scanner s, String text,int[] position,char o_d,Boolean[]validMovement,String[]readableMoves) {
        Boolean validOpt=false;
        String temp;
        while (!validOpt) {
            System.out.printf(YELLOW+text+RESET);
            temp = s.next();
            if (temp.equalsIgnoreCase("quit")) {
                System.out.println(GREEN+"You quit successfully!"+RESET);
                return true;
            }
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
                                readableMoves[0]=temp;
                            } else if (o_d=='d') {
                                position[2]=posX;
                                position[3]=posY;
                                readableMoves[1]=temp;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
        return false;
    }

    public Boolean newTurn(int p, String[] players, Scanner s,char[][]board,ArrayList<String> movements,ArrayList<Character> deadPieces,int[]gamesWon,ArrayList<Character>promotedPieces) {
        
        if (p==0) { // SI es el jugador BLANCO
            System.out.printf("\nIt's turn of %s%s%s: ",BOLD,players[p],RESET);
        } else if (p==1) { // SI es el jugador ROJO
            System.out.printf("\nIt's turn of %s%s%s%s: ",RED,BOLD,players[p],RESET);
        }
        Boolean[] validMovement = {false,false};
        int[] movement = new int[4];
        String[] readableMoves = new String[2];
        char[][] movingBoard = new char[8][8];
        if (!anyPossibleLegalMove(p,board)) {
            if (p==0) {
                gamesWon[1]+=1;
            } else {
                gamesWon[0]+=1;
            }
            System.out.println(RED+"\n(!) You are in checkmate..."+RESET);
            return true;
        } else {
        do {
            validMovement[0]=false;
            do {
                validMovement[1]=false;
                Boolean quit=readPosition(p,s, "\nPlease enter the position of the piece you wanna move: ",movement,'o',validMovement,readableMoves);
                if (quit) {
                    if (p==0) {
                        gamesWon[1]+=1;
                    } else {
                        gamesWon[0]+=1;
                    }
                    return true;
                }
                validOrigin(p,board,validMovement,movement);
            } while (!validMovement[0]);
            copyBoard(board,movingBoard);
            possibleMoves(p, movingBoard, movement,board);
            showBoard(movingBoard);
            do {
                readPosition(p,s, "\nPlease enter the position of the destionation ('X' to cancel first piece): ",movement,'d',validMovement,readableMoves);
                if (validMovement[0]) {
                    validDestination(p,board,movingBoard,validMovement,movement,deadPieces,s,promotedPieces);
                } else {
                    showBoard(board);
                }
            } while (!validMovement[1]);
        } while (!validMovement[0]);
        for (int i=0;i<readableMoves.length;i++) {
            movements.add(readableMoves[i]);
        }
        showBoard(board);
        }
        return false;
    }

    public void promotePawn(char[][]board,char piece,int dstX,int dstY,Scanner s,ArrayList<Character>promotedPieces) {
        System.out.println();
        System.out.println(GREEN+"(+) You can promote your pawn!"+RESET);
        System.out.println(GREEN+"1. (Tower)"+RESET);
        System.out.println(GREEN+"2. (Horse)"+RESET);
        System.out.println(GREEN+"3. (Bishop)"+RESET);
        System.out.println(GREEN+"4. (Queen)"+RESET);
        System.out.print(GREEN+"\nChoose what piece do you wanna get: "+RESET);
        Boolean validOpt=false;
        while (!validOpt) {
            try {
                int opt = s.nextInt();
                switch (opt) {
                    case 1:
                        changePiece(board, piece, dstX, dstY, 't',promotedPieces);
                        validOpt=true;
                        break;
                    case 2:
                        changePiece(board, piece, dstX, dstY, 'h',promotedPieces);
                        validOpt=true;
                        break;
                    case 3:
                        changePiece(board, piece, dstX, dstY, 'b',promotedPieces);
                        validOpt=true;
                        break;
                    case 4:
                        changePiece(board, piece, dstX, dstY, 'q',promotedPieces);
                        validOpt=true;
                        break;
                
                    default:
                        System.out.println(RED+"(!) Please enter a valid option."+RESET);
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(RED+"(!) Please enter a number."+RESET);
                s.nextLine();
            }
        }
        
    }

    public void changePiece(char[][]board,char piece,int dstX,int dstY,char newPiece,ArrayList<Character>promotedPieces) {
        if (Character.isUpperCase(piece)) { // si es blanca
            newPiece=Character.toUpperCase(newPiece);
            board[dstY][dstX]=newPiece;
        } else { // si es roja
            newPiece=Character.toLowerCase(newPiece);
            board[dstY][dstX]=newPiece;
        }
        promotedPieces.add(newPiece);
        System.out.println(GREEN+"(+) You successfully promoted your pawn to a "+newPiece+RESET);
    }

    public boolean anyPossibleLegalMove(int p, char[][] board) {

        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {

                if ((p == 0 && Character.isUpperCase(board[f][c])) ||
                    (p == 1 && Character.isLowerCase(board[f][c]))) {

                    int[] movement = {c, f};
                    char[][] mBoard = new char[8][8];
                    copyBoard(board, mBoard);

                    possibleMoves(p, mBoard, movement, board);

                    for (int y = 0; y < 8; y++) {
                        for (int x = 0; x < 8; x++) {

                            if (mBoard[y][x] == '*') {

                                if (isMoveLegal(p, board, c, f, x, y)) {
                                    return true; // hay al menos 1 movimiento
                                }
                            }
                        }
                    }
                }
            }
        }

        return false; // no hay movimientos
    }

    public boolean isKingInCheck(int p, char[][]board) {

        char[][] attackBoard = new char[8][8];
        copyBoard(board, attackBoard);

        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {

                if (p == 0 && Character.isLowerCase(board[f][c])) { // si le toca al blanco
                    int[] movement = {c, f};
                    possibleMoves(1, attackBoard, movement,board);
                }

                if (p == 1 && Character.isUpperCase(board[f][c])) { // si le toca al rojo
                    int[] movement = {c, f};
                    possibleMoves(0, attackBoard, movement,board);
                }
            }
        }

        // Localizar el rey del jugador p y comprobar si está atacado
        char king = ' ';

        if (p==0) {
            king='K';
        } else {
            king='k';
        }

        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (board[f][c] == king&&attackBoard[f][c] == '*') {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isMoveLegal(int p, char[][] board, int orgX, int orgY, int dstX, int dstY) {

        char[][] testBoard = new char[8][8];
        copyBoard(board, testBoard);

        testBoard[dstY][dstX] = testBoard[orgY][orgX]; // simula el movimiento sin ejecutarlo
        testBoard[orgY][orgX] = ' ';

        return !isKingInCheck(p, testBoard); // comprueba si sigo en jaque despues del movimiento
    }

    public void possibleMoves(int p,char[][]movingBoard,int[]movement,char[][]board) {
        int orgX=movement[0];
        int orgY=movement[1];

        switch (Character.toLowerCase(movingBoard[orgY][orgX])) {
            case 'p':
                pawnMoves(p,movingBoard,movement);
                break;
            case 't':
                towerMoves(p, movingBoard, movement,board);
                break;
            case 'h':
                horseMoves(p, movingBoard, movement,board);
                break;
            case 'b':
                bishopMoves(p, movingBoard, movement,board);
                break;
            case 'q':
                kingQueenMoves(p, movingBoard, movement,board);
                break;
            case 'k':
                kingQueenMoves(p, movingBoard, movement,board);
                break;
            default:
                break;
        }
        //showBoard(movingBoard);
    }

    public void validDestination(int p,char[][]board,char[][]mBoard,Boolean[]validMovement,int[]movement,ArrayList<Character>deadPieces,Scanner s,ArrayList<Character>promotedPieces) {
        int orgX=movement[0];
        int orgY=movement[1];
        int dstX=movement[2];
        int dstY=movement[3];
        char orgPiece = board[orgY][orgX];

        orgPiece = Character.toLowerCase(orgPiece);
        char destField = board[dstY][dstX];
        
        for (int f=0;f<board.length;f++) {
            for (int c=0;c<board[0].length;c++) {
                if (mBoard[f][c]=='*'&&f==dstY&&c==dstX) {
                    if(destField!=' ') {
                        eatPiece(p, destField, orgX, orgY, dstX, dstY, board, mBoard, validMovement, deadPieces,s,promotedPieces);
                    } else {
                        movePiece(p, board, mBoard, orgX, orgY, dstX, dstY, validMovement,s,promotedPieces);
                    }
                }
            }
        }
    }

    public void copyBoard (char[][]board,char[][]mBoard) {
        for (int i=0;i<board.length;i++) {
            for (int j=0;j<board.length;j++) {
                mBoard[i][j]=board[i][j];
            }
        }
    }

    public void horseMoves(int p, char[][] mBoard, int[] movement,char[][]board) {
        int[][] directions = {
            {2, 1},
            {-2, 1},
            {2, -1},
            {-2, -1},
            {1, 2},
            {1, -2},
            {-1, 2},
            {-1, -2}
        };
        findFields(p, mBoard, movement, directions,board);
    }

    public void bishopMoves(int p, char[][] mBoard, int[] movement,char[][]board) {
        int[][] directions = {
            {1, 1},
            {-1, -1},
            {-1, 1},
            {1, -1}
        };
        findFields(p, mBoard, movement, directions,board);
    }

    public void kingQueenMoves(int p, char[][] mBoard, int[] movement,char[][]board) {
        int[][] directions = {
            {1, 1},
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
        };
        findFields(p, mBoard, movement, directions,board);
    }

    public void towerMoves(int p, char[][] mBoard, int[] movement,char[][]board) {
        int[][] directions = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
        };
        findFields(p, mBoard, movement, directions,board);
    }

    public void findFields(int p,char[][]mBoard,int[]movement,int[][]directions,char[][]board) {
        int orgX = movement[0];
        int orgY = movement[1];

        for (int i = 0; i < directions.length; i++) {

            int stepX = directions[i][0];
            int stepY = directions[i][1];

            int x = orgX + stepX;
            int y = orgY + stepY;

            boolean stop = false;

            while (x >= 0 && x < 8 && y >= 0 && y < 8 && !stop) {

                char field = mBoard[y][x];

                if (field == ' ') {
                    markField(mBoard,y,x);
                } else {

                    if (((p == 0 && Character.isLowerCase(field))||
                    (p == 1 && Character.isUpperCase(field)))) {
                        markField(mBoard,y,x);
                    }

                    stop = true;
                }
                if (Character.toLowerCase(mBoard[orgY][orgX])!='h'&&Character.toLowerCase(mBoard[orgY][orgX])!='k') {
                    x += stepX;
                    y += stepY;
                }
            }
        }
    }

    public void pawnMoves(int p, char[][] mBoard, int[] movement) {

        int orgX = movement[0];
        int orgY = movement[1];

        int move;
        int startRow;

        if (p == 0) { // blancas
            move = -1;
            startRow = 6;
        } else { // rojas
            move = 1;
            startRow = 1;
        }

        int nextY = orgY + move;

        if (nextY >= 0 && nextY < 8) {

            if (mBoard[nextY][orgX] == ' ') {
                markField(mBoard, nextY, orgX);

                int doubleStepY = orgY + (move * 2);

                if (orgY == startRow && // si es la primera casilla doble movimiento
                    doubleStepY >= 0 && doubleStepY < 8 &&
                    mBoard[doubleStepY][orgX] == ' ') {

                    markField(mBoard, doubleStepY, orgX);
                }
            }

           
            if (orgX < 7) { // captura diagonal der
                char target = mBoard[nextY][orgX + 1];

                if ((p == 0 && Character.isLowerCase(target)) ||
                    (p == 1 && Character.isUpperCase(target))) {

                    markField(mBoard, nextY, orgX + 1);
                }
            }

           
            if (orgX > 0) { // captura diagonal izq
                char target = mBoard[nextY][orgX - 1];

                if ((p == 0 && Character.isLowerCase(target)) ||
                    (p == 1 && Character.isUpperCase(target))) {

                    markField(mBoard, nextY, orgX - 1);
                }
            }
        }
    }


    public void markField(char[][]board,int i,int j) {
        board[i][j]='*';    
    }

    public void movePiece(int p,char[][]board,char[][]movingBoard,int orgX,int orgY,int dstX,int dstY, Boolean[]validMovement,Scanner s,ArrayList<Character>promotedPieces) {
        if (isMoveLegal(p, board, orgX, orgY, dstX, dstY)) {
            char piece=board[orgY][orgX];
            board[dstY][dstX]=board[orgY][orgX];
            board[orgY][orgX]=' ';
            validMovement[1]=true;
            if (piece=='p'&&dstY==7) {
                promotePawn(board,piece,dstX,dstY,s,promotedPieces);
            } else if (piece=='P'&&dstY==0) {
                promotePawn(board,piece,dstX,dstY,s,promotedPieces);
            }
        } else {
            System.out.println(RED+"(!) Your king is in check!"+RESET);
        }
    }

    public boolean pathIsClear(int orgX, int orgY, int dstX, int dstY, char[][] board) {

        int stepX = Integer.compare(dstX, orgX);
        int stepY = Integer.compare(dstY, orgY);

        int x = orgX + stepX;
        int y = orgY + stepY;

        while (x != dstX || y != dstY) {
            if (board[y][x] != ' ') {
                System.out.println(RED+"(!) You cannot go through other pieces."+RESET);
                return false; // Hay pieza en medio
            }
            x += stepX;
            y += stepY;
        }

        return true; // Camino libre
    }


    public void eatPiece(int p, char destField, int orgX, int orgY, int dstX, int dstY, char[][]board,char[][]movingBoard, Boolean[]validMovement,ArrayList<Character>deadPieces,Scanner s,ArrayList<Character>promotedPieces) {
        if ((p==0 && Character.isLowerCase(destField))||(p==1 && Character.isUpperCase(destField))) {
            if (isMoveLegal(p, board, orgX, orgY, dstX, dstY)) {
                char piece=board[orgY][orgX];
                deadPieces.add(destField);
                board[dstY][dstX]=board[orgY][orgX];
                board[orgY][orgX]=' ';
                validMovement[1]=true;
                if (piece=='p'&&dstY==7) {
                    promotePawn(board,piece,dstX,dstY,s,promotedPieces);
                } else if (piece=='P'&&dstY==0) {
                    promotePawn(board,piece,dstX,dstY,s,promotedPieces);
                }
            } else {
                System.out.println(RED+"(!) Your king is in check!"+RESET);
            }
        } else {
            System.out.println(RED+"(!) You cannot eat your pieces.");
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
