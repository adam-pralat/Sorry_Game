import java.util.Scanner;

public class Sorry {
    // Run method
    public static void main(String[] args) {
        // Game rules
        Sorry game = new Sorry();
        System.out.println("Welcome to the game 'Sorry!'");
        System.out.println("Object of the Game:");
        System.out.println("Be the first player to get all four of your pawns from your start area to your home.");
        System.out.println("Choose a player to go first. Each player, in turn, draws one card from the deck and follows its instructions. To beg...");
        System.out.println("A pawn can jump over any other pawn during its move. However, two pawns cannot occupy the same square; a pawn that l...");
        System.out.println("If a pawn lands at the start of a slide, it immediately \"slides\" to the last square of the slide.");
        System.out.println("Good luck!");
        System.out.println("Type anything to start.");
        game.scanner.next();

        game.printBoard();
        while (game.checkWin() == 0) {
            game.takeTurn();
        }
    }

    private String[][] board = new String[16][16];
    private int[][] pawns = new int[16][16];
    private int[][] temp = new int[16][16];
    private char[][] tiles = new char[16][16];
    private int turn = 1;
    private int tester = 0;
    private int inpRow = -1;
    private int inpCol = -1;
    Scanner scanner = new Scanner(System.in);

    // Constructor
    public Sorry() {

        // Draws Slides
        for (int i = 1; i < 14; i++) {
            if (i < 5 || i > 8) {
                board[0][i] = "►";
                board[i][15] = "▼";
                board[15][15 - i] = "◄";
                board[15 - i][0] = "▲";
            }
        }

        // Draws safety and start zones
        for (int i = 1; i < 6; i++) {
            board[13][i] = "⇨";
            board[i][2] = "⇩";
            board[2][15 - i] = "⇦";
            board[15 - i][13] = "⇧";
            board[4][15 - i] = "⇨";
            board[15 - i][11] = "⇩";
            board[11][i] = "⇦";
            board[i][4] = "⇧";
        }

        // Draws home/finish line
        board[13][6] = "🚩";
        board[6][2] = "🚩";
        board[2][9] = "🚩";
        board[9][13] = "🚩";

        // Draws Gameboard text
        board[7][5] = "\u001B[31mS";
        board[7][6] = "\u001B[32mO";
        board[7][7] = "\u001B[33mR";
        board[7][8] = "\u001B[34mR";
        board[7][9] = "\u001B[35mY";
        board[7][10] = "\u001B[36m!\u001B[0m";

        // Defines tiles which pawns can move on
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                if (row == 0 || row == 15 || col == 0 || col == 15) {
                    tiles[row][col] = 'Y';
                }
            }
        }

        // Shapes rest of board
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                if (board[row][col] == null) {
                    if (tiles[row][col] == 'Y') {
                        board[row][col] = "▫";
                    } else {
                        board[row][col] = " ";
                    }

                }
            }
        }

        // Places starting Pawns
        for (int i = 1; i < 5; i++) {
            pawns[i][4] = 1;
            pawns[4][15 - i] = 2;
            pawns[15 - i][11] = 3;
            pawns[11][i] = 4;

        }

    }

    // Method for printing the board array on to the console


    public void printBoard() {
        // Clears console
        System.out.print("\033[H\033[2J");
        // Prints letters on sides (column coordinates)
        System.out.print(" ");
        for (int col = 0; col < 16; col++) {
            System.out.print(" " + (char) (col + 97));
        }
        System.out.println();
        // Prints board
        for (int row = 0; row < 16; row++) {
            System.out.print((char) (row + 65) + " "); // Row coordinates
            for (int col = 0; col < 16; col++) {
                if (temp[row][col] != 0) {
                    System.out.print(temp[row][col] + " "); // If there is a temporary pawn, print it

                } else if (pawns[row][col] != 0) {
                    System.out.print(pawns[row][col] + " "); // else if there is a regular pawn, print it
                } else {
                    System.out.print(board[row][col] + " "); // else print the game board
                }

            }
            System.out.println();

        }
    }

    // Method for adding a delay


    public static void wait(int ms) {
        try {
            Thread.sleep(ms);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();

        }

    }

    // Method for getting Yes or No input from user


    public boolean yesOrNo() {
        System.out.println("(Type 'Y' for YES or 'N' for NO).");
        while (true) {
            char input = scanner.next().charAt(0);
            if (input == 'Y') {
                return true;

            } else if (input == 'N') {
                return false;

            } else {
                System.out.println("Invalid option.");

            }

        }
    }

    // Method for drawing a random card
    //


    public int drawCard() {
        int randInt = -1;
        while (randInt == -1 || randInt == 6 || randInt == 9) {
            randInt = (int) (Math.random() * (13) + 1);

        }
        return randInt;

    }
    //

    // Method for testing all card scenarios
    /*
    public int drawCard() {
        tester++;

        while(tester == 6 || tester == 9) {
            tester++;
        }
        if(tester == 14) {
            tester = 1;
        }
        return tester;
    }
    */

    // Method for moving pieces up the start space
    public void updateStart() {
        for (int x = 0; x < 3; x++) {
            for (int i = 1; i < 5; i++) {
                if (pawns[i][4] == 0 && pawns[i + 1][4] != 0) {
                    pawns[i][4] = 1;
                    pawns[i + 1][4] = 0;
                }
                if (pawns[4][15 - i] == 0 && pawns[4][14 - i] != 0) {
                    pawns[4][15 - i] = 2;
                    pawns[4][14 - i] = 0;

                }
                if (pawns[15 - i][11] == 0 && pawns[14 - i][11] != 0) {
                    pawns[15 - i][11] = 3;
                    pawns[14 - i][11] = 0;

                }
                if (pawns[11][i] == 0 && pawns[11][i + 1] != 0) {
                    pawns[11][i] = 4;
                    pawns[11][i + 1] = 0;

                }

            }

        }

    }

    // Method for moving pieces up the finish space


    public void updateFinish() {
        for (int x = 0; x < 4; x++) {
            for (int i = 5; i > 1; i--) {
                if (pawns[i][2] == 0 && pawns[i - 1][2] != 0) {
                    pawns[i][2] = 1;
                    pawns[i - 1][2] = 0;

                }
                if (pawns[2][15 - i] == 0 && pawns[2][16 - i] != 0) {
                    pawns[2][15 - i] = 2;
                    pawns[2][16 - i] = 0;

                }
                if (pawns[15 - i][13] == 0 && pawns[16 - i][13] != 0) {
                    pawns[15 - i][13] = 3;
                    pawns[16 - i][13] = 0;

                }
                if (pawns[13][i] == 0 && pawns[13][i - 1] != 0) {
                    pawns[13][i] = 4;
                    pawns[13][i - 1] = 0;

                }

            }

        }

    }

    // Method for checking if there are any pawns at the start area


    public boolean checkStart() {
        boolean check = false;
        if (turn == 1) {
            if (pawns[1][4] == 1) {
                check = true;

            }

        } else if (turn == 2) {
            if (pawns[4][14] == 2) {
                check = true;

            }

        } else if (turn == 3) {
            if (pawns[14][11] == 3) {
                check = true;

            }

        } else if (turn == 4) {
            if (pawns[11][1] == 4) {
                check = true;

            }

        }
        if (check == true) {
            System.out.println("Do you want to move a new piece from the start?");
            return yesOrNo();

        }
        return false;

    }

    // Method for moving a pawn from start


    public void moveStart() {
        if (turn == 1) {
            pawns[0][4] = 1;
            pawns[1][4] = 0;

        } else if (turn == 2) {
            pawns[4][15] = 2;
            pawns[4][14] = 0;
        } else if (turn == 3) {
            pawns[15][11] = 3;
            pawns[14][11] = 0;

        } else {
            pawns[11][0] = 4;
            pawns[11][1] = 0;

        }
        bumpCheck();
        updateStart();

    }

    // Checks if any pawns on board can move


    public int checkBoard() {
        int check = 0;
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                if (tiles[row][col] == 'Y') {
                    if (pawns[row][col] == turn) {
                        System.out.println("Potential move: " + (char) (row + 65) + ", " + (char) (col + 97));
                        check++;

                    }

                }

            }

        }
        if (check == 0) {

            System.out.println("Player " + turn + " has no possible moves. Type something to procede to next players turn.");
            scanner.next();

        }
        return check;

    }

    // Method for checking if any pawns were 'bumped'

    public void bumpCheck() {
        for (int player = 1; player < 5; player++) {
            int check = 0;
            while (check < 4) {
                check = 0;
                for (int row = 0; row < 16; row++) {
                    for (int col = 0; col < 16; col++) {
                        if (pawns[row][col] == player) {
                            check++;
                        }

                    }

                }
                // If there are less than four pawns on the board, add another pawn to the start
                if (check < 4) {
                    if (player == 1) {
                        pawns[4][4] = 1;

                    } else if (player == 2) {
                        pawns[4][11] = 2;
                    } else if (player == 3) {
                        pawns[11][11] = 3;
                    } else {
                        pawns[11][4] = 4;
                    }
                    updateStart();
                    System.out.println("Sorry!");
                    wait(3000);

                }
            }

        }

    }

    // Method for moving pawns one space

    public void moveOne(int row, int col, int dir) {
        // If pawn is at finish tile, enter finish space
        if (temp[0][2] == 1) {
            temp[1][2] = 1;

        } else if (temp[2][15] == 2) {
            temp[2][14] = 2;

        } else if (temp[15][13] == 3) {
            temp[14][13] = 3;

        } else if (temp[13][0] == 4) {
            temp[13][1] = 4;
        }
        // Otherwise move it around board
        else if (row == 0 && ((col > 0 && dir == -1) || (col < 15 && dir == 1))) {
            temp[row][col + dir] = temp[row][col];
            inpCol = col + dir;
        } else if (col == 15 && ((row > 0 && dir == -1) || (row < 15 && dir == 1))) {
            temp[row + dir][col] = temp[row][col];
            inpRow = row + dir;
        } else if (row == 15 && ((col > 0 && dir == 1) || (col < 15 && dir == -1))) {
            temp[row][col - dir] = temp[row][col];
            inpCol = col - dir;
        } else {
            temp[row - dir][col] = temp[row][col];
            inpRow = row - dir;
        }
        temp[row][col] = 0;
        printBoard();
        wait(500);
    }

    // Method for moving pawns (by moving the pawns one space multiple times)
    public void move(int dis) {
        temp[inpRow][inpCol] = pawns[inpRow][inpCol];
        pawns[inpRow][inpCol] = 0;
        for (int i = 0; i < Math.abs(dis); i++) {
            moveOne(inpRow, inpCol, dis / Math.abs(dis));
        }

        // If pawn lands on slide start, move down slide
        if (temp[0][1] != 0) {
            temp[0][4] = temp[0][1];
            temp[0][1] = 0;
        }
        if (temp[0][9] != 0) {
            temp[0][13] = temp[0][9];
            temp[0][9] = 0;
        }
        if (temp[1][15] != 0) {
            temp[4][15] = temp[1][15];
            temp[1][15] = 0;
        }
        if (temp[9][15] != 0) {
            temp[13][15] = temp[9][15];
            temp[9][15] = 0;
        }
        if (temp[15][14] != 0) {
            temp[15][11] = temp[15][14];
            temp[15][14] = 0;
        }
        if (temp[15][6] != 0) {
            temp[15][2] = temp[15][6];
            temp[15][6] = 0;
        }
        if (temp[14][0] != 0) {
            temp[11][0] = temp[14][0];
            temp[14][0] = 0;
        }
        if (temp[6][0] != 0) {
            temp[2][0] = temp[6][0];
            temp[6][0] = 0;

        }

        // Saves the final temporary locations as regular locations
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                if (temp[row][col] == turn) {
                    pawns[row][col] = temp[row][col];
                    temp[row][col] = 0;
                }
            }
        }
        bumpCheck();
        updateFinish();
        inpRow = -1;
        inpCol = -1;
    }

    // Method for choosing which pawn to move
    public void choosePawn() {
        while ((inpRow == -1 || inpCol == -1) || pawns[inpRow][inpCol] != turn || tiles[inpRow][inpCol] != 'Y') {
            inpRow = -1;
            inpCol = -1;
            while (inpRow < 0 || inpRow > 15) {
                System.out.print("Enter the row of the pawn you want to move: ");
                inpRow = (int) scanner.next().charAt(0) - 65;
                if (inpRow < 0 || inpRow > 15) {
                    System.out.println("Invalid row.");
                }
            }
            while (inpCol < 0 || inpCol > 15) {
                System.out.print("Enter the column of the pawn you want to move: ");
                inpCol = (int) scanner.next().charAt(0) - 97;
                if (inpCol < 0 || inpCol > 15) {
                    System.out.println("Invalid column.");
                }
            }
            if (pawns[inpRow][inpCol] != turn || tiles[inpRow][inpCol] != 'Y') {
                System.out.println("Invalid location.");
            }
        }
    }

    // Method for checking if a player has won
    public int checkWin() {
        int winner = 0;
        if (pawns[2][2] == 1) {
            winner = 1;

        }
        if (pawns[2][13] == 2) {
            winner = 2;

        }
        if (pawns[13][13] == 3) {
            winner = 3;
        }
        if (pawns[13][2] == 4) {
            winner = 4;
        }
        if (winner != 0) {
            System.out.println("Player " + winner + " wins the game. Congratulations!");
        }
        return winner;
    }

    // Method for all steps during a turn
    public void takeTurn() {
        //
        System.out.println("\nPlayer " + turn + ":");
        int card = drawCard();

        if (card == 1) {
            System.out.println("1. Either move a pawn from Start or move a pawn one space forward.");
            if (checkStart()) {
                moveStart();
            } else if (checkBoard() > 0) {
                choosePawn();
                move(1);
            }
        } else if (card == 2) {
            System.out.println("2. Either move a pawn from Start or move a pawn two spaces forward. Draw again.");
            if (checkStart()) {
                moveStart();
            } else if (checkBoard() > 0) {
                choosePawn();
                move(2);
            }
            turn--;
        } else if (card == 4) {
            System.out.println("4. Move a pawn four spaces backward.");
            if (checkBoard() > 0) {
                choosePawn();
                move(-4);
            }
        } else if (card == 7) {
            System.out.println("7. Move one pawn seven spaces forward, or split the seven spaces between two pawns.");
            int possibleMoves = checkBoard();
            if (possibleMoves > 1) {
                choosePawn();
                int tempRow = inpRow;
                int tempCol = inpCol;
                inpRow = -1;
                inpCol = -1;
                int amount = -1;
                while (amount < 0 || amount > 7) {
                    System.out.println("How far do you want to move this pawn?");
                    amount = scanner.nextInt();
                }
                if (amount < 7) {

                    System.out.println("Which pawn would you like to move the remaining " + (7 - amount) + " spaces.");
                    choosePawn();
                    move(7 - amount);
                }
                inpRow = tempRow;
                inpCol = tempCol;
                move(amount);
            } else if (possibleMoves > 0) {
                choosePawn();
                move(7);
            }
        } else if (card == 10) {
            System.out.println("10. Move a pawn ten spaces forward or one space backward.");
            if (checkBoard() > 0) {
                choosePawn();
                System.out.println("Type '1' to move ten spaces forward or '2' to move one space backward.");
                char input = 0;
                while (true) {
                    input = scanner.next().charAt(0);
                    if (input == '1') {
                        move(10);
                        break;
                    } else if (input == '2') {
                        move(-1);
                        break;
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
            }
        } else if (card == 11) {
            System.out.println("11. Move eleven spaces forward, or switch the places of one your pawns and an opponent's pawn.");
            if (checkBoard() > 0) {
                int curTurn = turn;
                for (int i = 0; i < 3; i++) {
                    turn++;
                    if (turn == 5) {
                        turn = 1;
                    }
                    System.out.println("Potential swaps with player " + turn + ":");
                    int check = checkBoard();
                    if (check > 0) {
                        System.out.println("Do you want to switch with any of these pawns?");
                        if (yesOrNo()) {
                            System.out.println("Which pawn (opponent) do you want to switch with?");
                            choosePawn();
                            int tempRow = inpRow;
                            int tempCol = inpCol;
                            inpRow = -1;
                            inpCol = -1;
                            turn = curTurn;
                            System.out.println("Choose your pawn that you want to switch.");
                            choosePawn();
                            pawns[inpRow][inpCol] = pawns[tempRow][tempCol];
                            pawns[tempRow][tempCol] = turn;
                            inpRow = -1;
                            inpCol = -1;
                            break;
                        }
                    }
                }
                if (turn != curTurn) {
                    turn = curTurn;
                    choosePawn();
                    move(11);
                }
            }
        } else if (card == 13) {
            System.out.println("Sorry! Card. Take any one pawn from Start and move it directly to a square occupied by any opponent's pawn, send...");
            if (checkStart()) {
                // Goes through each players pawns and checks if they can be swapped
                int curTurn = turn;
                for (int i = 0; i < 3; i++) {
                    turn++;
                    if (turn == 5) {
                        turn = 1;
                    }
                    System.out.println("Potential swaps with player " + turn + ":");
                    int check = checkBoard();
                    if (check > 0) {
                        System.out.println("Do you want to switch with any of these pawns?");
                        if (yesOrNo()) {
                            System.out.println("Which pawn (opponent) do you want to switch with?");
                            choosePawn();
                            pawns[inpRow][inpCol] = curTurn;
                            inpRow = -1;
                            inpCol = -1;
                            turn = curTurn;
                            // Removes one pawn from start
                            if (turn == 1) {
                                pawns[1][4] = 0;
                            } else if (turn == 2) {
                                pawns[4][14] = 0;
                            } else if (turn == 3) {
                                pawns[14][11] = 0;
                            } else {
                                pawns[11][1] = 0;
                            }
                            updateStart();
                            bumpCheck();
                            break;
                        }
                    }
                }
            } else {
                System.out.println("No possible moves. Type something to procede to next players turn.");
                scanner.next();
            }
        } else {
            System.out.println(card + ". Move a pawn " + card + " spaces forward.");
            if (checkBoard() > 0) {
                choosePawn();
                move(card);
            }
        }
        printBoard();
        turn++;
        if (turn == 5) {
            turn = 1;
        }
    }
}