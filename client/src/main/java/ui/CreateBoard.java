package ui;

import chess.ChessGame;
import chess.ChessPiece;
import static ui.EscapeSequences.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class CreateBoard {
    //ChessGame game;
    private ChessPiece[][] board;
    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public CreateBoard(ChessGame chessGame) {
        board = chessGame.getBoard().getBoard();
    }

    public void createRegBoard() {
        out.print("\n");
        drawLetters();
        int rowNum = 0;

        for (ChessPiece[] row : board) {
            drawSideNumInverted(rowNum);
            int colNum = 0;

            for (ChessPiece col : row) {
                createSquare(rowNum, colNum);
                colNum++;
            }

            drawSideNum(rowNum);
            setBlack();
            out.print("\n");
            rowNum++;
        }

        drawLetters();
        out.print(RESET_BG_COLOR);
    }

    public void createInvertedBoard() {
        out.print("\n");
        drawLettersInverted();
        int rowNum = 7;

        for (ChessPiece[] row : board) {
            drawSideNum(7 - rowNum);
            int colNum = 7;

            for (ChessPiece col : row) {
                createSquare(rowNum, colNum);
                colNum--;
            }

            drawSideNumInverted(7 - rowNum);
            setBlack();
            out.print("\n");
            rowNum--;
        }

        drawLettersInverted();
        out.print(RESET_BG_COLOR);
    }

    private void drawLetters() {
        setLightGray();
        out.print("    a  b  c  d  e  f  g  h    ");
        setBlack();
        out.print("\n");
    }

    private void drawLettersInverted() {
        setLightGray();
        out.print("    h  g  f  e  d  c  b  a    ");
        setBlack();
        out.print("\n");
    }

    private void drawSideNum(int row) {
        setLightGray();
        out.print(" " + (row + 1) + " ");
    }

    private void drawSideNumInverted(int row) {
        setLightGray();
        out.print(" " + (8 - row) + " ");
    }

    private void createSquare(int row, int col) {
        ChessPiece piece = board[row][col];
        if ((row + col) % 2 != 0) {
            setBlack();
        }
        else {
            setWhite();
        }
        fillSquare(piece);
    }

    private void fillSquare(ChessPiece chessPiece) {
        out.print(" ");
        if (chessPiece == null) {
            out.print(" ");
        }
        else {
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                out.print(SET_TEXT_COLOR_BLUE);
            }
            else {
                out.print(SET_TEXT_COLOR_RED);
            }
            out.print(chessPiece);
        }
        out.print(" ");
    }

    private void setLightGray() {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void setBlack() {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void setWhite() {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
