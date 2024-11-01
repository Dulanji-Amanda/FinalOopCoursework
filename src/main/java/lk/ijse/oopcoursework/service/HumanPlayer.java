package lk.ijse.oopcoursework.service;

public class HumanPlayer extends Player {
    boolean legelMove;

    public HumanPlayer(Board board) {
        super(board);
    }

    @Override
    public void move(int row, int col) {
        legelMove = board.isLegalMove(row, col);
        if (legelMove) {
            board.updateMove(row, col, Piece.X);
            board.getBoardUI().update(row, col, true);
        }
    }
}
