package lk.ijse.oopcoursework.service;

public interface BoardUI {
    void update(int col, int row, boolean isHuman);
    void notifyWinner(Piece winner);
}