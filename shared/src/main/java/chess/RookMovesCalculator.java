package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends MoveFunctionsCalculator {

    private final int[][] possibleDirections = { {1,0}, {0,1}, {-1,0}, {0,-1} };


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return calculateMovesQRB(board, myPosition, possibleDirections);
    }
}
