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
        String[] players = new String[2];
        int[] gamesWon = {0,0};
        ArrayList<String> movements = new ArrayList<String>();
        ArrayList<Character> deadPieces = new ArrayList<Character>();
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
                    finished=newTurn(p,players,s,board,movements,deadPieces,gamesWon);
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

    public Boolean newTurn(int p, String[] players, Scanner s,char[][]board,ArrayList<String> movements,ArrayList<Character> deadPieces,int[]gamesWon) {
        
        if (p==0) { // SI es el jugador BLANCO
            System.out.printf("\nIt's turn of %s%s%s: ",BOLD,players[p],RESET);
        } else if (p==1) { // SI es el jugador ROJO
            System.out.printf("\nIt's turn of %s%s%s%s: ",RED,BOLD,players[p],RESET);
        }
        Boolean[] validMovement = {false,false};
        int[] movement = new int[4];
        String[] readableMoves = new String[2];
        char[][] movingBoard = new char[8][8];
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
            possibleMoves(p, movingBoard, movement);
            showBoard(movingBoard);
            do {
                readPosition(p,s, "\nPlease enter the position of the destionation ('X' to cancel first piece): ",movement,'d',validMovement,readableMoves);
                if (validMovement[0]) {
                    validDestination(p,board,validMovement,movement,deadPieces);
                } else {
                    showBoard(board);
                }
            } while (!validMovement[1]);
        } while (!validMovement[0]);
        for (int i=0;i<readableMoves.length;i++) {
            movements.add(readableMoves[i]);
        }
        showBoard(board);
        return false;
    }

    public void possibleMoves(int p,char[][]movingBoard,int[]movement) {
        int x=movement[0];
        int y=movement[1];

        switch (Character.toLowerCase(movingBoard[y][x])) {
            case 'p':
                pawnMoves(p,movingBoard,movement);
                break;
            case 't':
                towerMoves(p, movingBoard, movement);
                break;
            case 'h':
                horseMoves(p, movingBoard, movement);
                break;
            case 'b':
                bishopMoves(p, movingBoard, movement);
                break;
            default:
                break;
        }
    }

    public void validDestination(int p,char[][]board,Boolean[]validMovement,int[]movement,ArrayList<Character>deadPieces) {
        int posXorg=movement[0];
        int posYorg=movement[1];
        int posXdest=movement[2];
        int posYdest=movement[3];
        char orgPiece = board[posYorg][posXorg];

        orgPiece = Character.toLowerCase(orgPiece);


        switch (orgPiece) {
            case 'p': // PAWNS
                validPawnMove(p,board, movement, posXorg, posYorg, posXdest, posYdest,validMovement,deadPieces);
                break;
            case 't': // TOWERS
                validTowerMove(p,board, movement, posXorg, posYorg, posXdest, posYdest,validMovement,deadPieces);
                break;
            case 'h': // HORSES
                validHorseMove(p,board, movement, posXorg, posYorg, posXdest, posYdest,validMovement,deadPieces);
                break;
            case 'b': // BISHOPS
                validBishopMove(p,board, movement, posXorg, posYorg, posXdest, posYdest,validMovement,deadPieces);
                break;
            case 'k': // KINGS
                
                break;
            case 'q': // QUEENS
                
                break;
        
            default:
                break;
        }
    }

    public void validHorseMove(int p,char[][]board,int[]movement,int orgX,int orgY, int dstX, int dstY,Boolean[]validMovement,ArrayList<Character>deadPieces) {

        char destField = board[dstY][dstX];
        
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

        int possibleMoves=0;

        for (int i = 0; i < directions.length; i++) {

            if((dstY==orgY+directions[i][0])&&(dstX==orgX+directions[i][1])) {
                if(destField==' ') {
                    possibleMoves+=1;
                    movePiece(board, orgX, orgY, dstX, dstY, validMovement);
                } else {
                    possibleMoves+=1;
                    eatPiece(p, destField, orgX, orgY, dstX, dstY, board, validMovement, deadPieces);
                }
            }
        }
        if (possibleMoves==0) {
            System.out.println(RED+"(!) You cannot move there."+RESET);
        }
    }

    public void validBishopMove(int p,char[][]board,int[]movement,int orgX,int orgY,int dstX,int dstY,Boolean[]validMovement,ArrayList<Character>deadPieces) {

        char destField = board[dstY][dstX];

        int[][] directions = {
            {1, 1},
            {-1, -1},
            {-1, 1},
            {1, -1}
        };

        int possibleMoves = 0;

        for (int i = 0; i < directions.length; i++) {

            int stepX = directions[i][0];
            int stepY = directions[i][1];

            int x = orgX + stepX;
            int y = orgY + stepY;

            boolean stop = false;

            while (x >= 0 && x < 8 && y >= 0 && y < 8 && !stop) {

                if (board[y][x] != ' ') { // si hay pieza bloquear

                    if (x == dstX && y == dstY) { // si es el destino
                        possibleMoves += 1;
                        eatPiece(p, destField, orgX, orgY, dstX, dstY, board, validMovement, deadPieces);
                    }

                    stop = true; // para si hay una pieza
                } else {
                    if (x == dstX && y == dstY) { // si es el destino y esta vacio
                        possibleMoves += 1;
                        movePiece(board, orgX, orgY, dstX, dstY, validMovement);
                        stop = true;
                    }
                }

                x += stepX;
                y += stepY;
            }
        }

        if (possibleMoves == 0) {
            System.out.println(RED + "(!) You cannot move there." + RESET);
        }
    }


    public void validTowerMove(int p,char[][]board,int[]movement,int orgX,int orgY, int dstX, int dstY,Boolean[]validMovement,ArrayList<Character>deadPieces) {

        char destField = board[dstY][dstX];

        if (orgX==dstX&&orgY==dstY) {
            System.out.println(RED+"(!) You cannot move to the same position."+RESET);
        } else {

            // movimiento de comer
            if ((orgX==dstX||orgY==dstY)&& // Si es recto (hor/ver)
                destField!=' ' // Si el destino no esta vacio
            ){
                if(pathIsClear(orgX, orgY, dstX, dstY, board)) {
                    eatPiece(p,destField,orgX,orgY,dstX,dstY,board,validMovement,deadPieces);
                }
            } else if ((orgX==dstX||orgY==dstY)&&
            pathIsClear(orgX, orgY, dstX, dstY, board)&&
            destField==' ') { // movimiento normal
                movePiece(board, orgX, orgY, dstX, dstY, validMovement);
            } else if (orgX!=dstX||orgY!=dstY) {
                System.out.println(RED+"(!) You cannot move there."+RESET);
            }
            
            
        }
    }

    public void validPawnMove(int p,char[][]board,int[]movement,int orgX,int orgY, int dstX, int dstY,Boolean[]validMovement,ArrayList<Character>deadPieces) {

        int startPos,move;

        if(p==0) {
            startPos=6;
            move=-1;
        } else {
            startPos=1;
            move=1;
        }

        char destField = board[dstY][dstX];
        char midField = ' ';
        if(dstY!=0) {
            midField = board[dstY-1][dstX];
        }

        // movimiento de comer
        if ((orgX-1==dstX||orgX+1==dstX)&& // Si en diagonal 1 casilla
            destField!=' '&& // Si el destino no esta vacio
            dstY==orgY+move // Si se mueve 1 casilla
        ){
            eatPiece(p,destField,orgX,orgY,dstX,dstY,board,validMovement,deadPieces);
        } else if (orgX==dstX) {// Si va recto
            if ((destField!=' '||midField!=' ')&&(dstY==orgY+move+move||dstY==orgY+move)) {
                System.out.println(RED+"(!) You cant move into another piece."+RESET);
            }
            // movimiento de 2
            if (
                destField==' '&& // Si el destino esta vacio
                midField==' '&& // Si la casilla de en medio está vacia
                orgY==startPos&& // Si está en la primera casilla
                dstY==orgY+move+move // Si se mueve 2 casilla
            ) {
                board[dstY][dstX]=board[orgY][orgX];
                board[orgY][orgX]=' ';
                validMovement[1]=true;
            } else if (destField==' '&&midField==' '&&dstY==orgY+move+move) {
                System.out.println(RED+"(!) You are not in the first position."+RESET);
            }

            // movimiento de 1
            if(destField==' '&& // Si el destino esta vacio
                dstY==orgY+move // Si se mueve 1 casilla
            ){
                movePiece(board, orgX, orgY, dstX, dstY, validMovement);
            }
        } else {
            System.out.println(RED+"(!) You can only move straight."+RESET);
        }
        
    }

    public void copyBoard (char[][]board,char[][]mBoard) {
        for (int i=0;i<board.length;i++) {
            for (int j=0;j<board.length;j++) {
                mBoard[i][j]=board[i][j];
            }
        }
    }

    public void horseMoves(int p, char[][] mBoard, int[] movement) {

        int orgX = movement[0];
        int orgY = movement[1];

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
                }
                else {

                    if (
                        (p == 0 && Character.isLowerCase(field)) ||
                        (p == 1 && Character.isUpperCase(field))
                    ) {
                        markField(mBoard,y,x);
                    }

                    stop = true;
                }
            }
        }
    }

    public void bishopMoves(int p, char[][] mBoard, int[] movement) {

        int orgX = movement[0];
        int orgY = movement[1];

        int[][] directions = {
            {1, 1},
            {-1, -1},
            {-1, 1},
            {1, -1}
        };

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

                    if (
                        (p == 0 && Character.isLowerCase(field)) ||
                        (p == 1 && Character.isUpperCase(field))
                    ) {
                        markField(mBoard,y,x);
                    }

                    stop = true;
                }

                x += stepX;
                y += stepY;
            }
        }
    }
    public void towerMoves(int p, char[][] mBoard, int[] movement) {

        int orgX = movement[0];
        int orgY = movement[1];

        int[][] directions = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
        };

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
                }
                else {

                    if (
                        (p == 0 && Character.isLowerCase(field)) ||
                        (p == 1 && Character.isUpperCase(field))
                    ) {
                        markField(mBoard,y,x);
                    }

                    stop = true;
                }

                x += stepX;
                y += stepY;
            }
        }
    }

    public void pawnMoves(int p,char[][]mBoard,int[]movement) {

        int orgX=movement[0];
        int orgY=movement[1];

        int startPos,move;

        if(p==0) {
            startPos=6;
            move=-1;
        } else {
            startPos=1;
            move=1;
        }

        char eat1=' ';
        char eat2=' ';
        int pos1=0;
        int pos2=0;

        if (orgX!=7) {
            pos1=1;
            eat1=mBoard[orgY+move][orgX+1]; // COMER *
        } 
        if (orgX!=0) {
            pos2=-1;
            eat2=mBoard[orgY+move][orgX-1]; // COMER *
        }

        if (orgY==startPos&&mBoard[orgY+(move*2)][orgX]==' ') { // PRIMERA POSICION
            markField(mBoard,orgY+(move*2),orgX);
            markField(mBoard,orgY+move,orgX);
        } else if (mBoard[orgY+move][orgX]==' '){
            markField(mBoard,orgY+move,orgX);
        }
        try {
            if ((mBoard[orgY+move][orgX+pos1]!=' '||mBoard[orgY+move][orgX+pos2]!=' ')) {
                if ((p==0 && Character.isLowerCase(eat1))||(p==1 && Character.isUpperCase(eat1))) {
                    markField(mBoard, orgY+move, orgX+pos1);
                } else if ((p==0 && Character.isLowerCase(eat2))||(p==1 && Character.isUpperCase(eat2))) {
                    markField(mBoard, orgY+move, orgX+pos2);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        
    }

    public void markField(char[][]board,int i,int j) {
        board[i][j]='*';    
    }

    public void movePiece(char[][]board,int orgX,int orgY,int dstX,int dstY, Boolean[]validMovement) {
        board[dstY][dstX]=board[orgY][orgX];
        board[orgY][orgX]=' ';
        validMovement[1]=true;
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


    public void eatPiece(int p, char destField, int orgX, int orgY, int dstX, int dstY, char[][]board, Boolean[]validMovement,ArrayList<Character>deadPieces) {
        if ((p==0 && Character.isLowerCase(destField))||(p==1 && Character.isUpperCase(destField))) {
            deadPieces.add(destField);
            board[dstY][dstX]=board[orgY][orgX];
            board[orgY][orgX]=' ';
            validMovement[1]=true;
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
