public class BoardCompression {
    public static String getFullFEN(Piece[][] board, boolean turn, int halfmoveCounter, int moveNumber) {
        String compression = "";

        compression += getPiecePlacement(board);
        compression += turn ? " w " : " b ";
        compression += getCastlingRights(board);
        compression += ' ';
        compression += getEnPassantRights(board);

        compression += " " + halfmoveCounter;
        compression += " " + moveNumber;

        return compression;
    }

    public static String compressBoard(Piece[][] board) {
        String compression = "";

        compression += getPiecePlacement(board) + ' ';
        compression += getCastlingRights(board) + ' ';
        compression += getEnPassantRights(board);

        return compression;
    }

    private static String getPiecePlacement(Piece[][] board) {
        StringBuilder board_compression = new StringBuilder();
        int empty_squares = 0;

        for (int i = 0; i < 8; i++) {
            empty_squares = 0;
            for (int j = 0; j < 8; j++) {
                if (board[j][i] == null) {
                    empty_squares++;
                } else {
                    if (empty_squares != 0) {
                        board_compression.append(empty_squares);
                    }
                    board_compression.append(board[j][i].getAbbreviation());
                    empty_squares = 0;
                }
            }

            if (empty_squares != 0) {
                board_compression.append(empty_squares);
            }

            if (i != 7) {
                board_compression.append('/');
            }
        }

        return board_compression.toString();
    }

    private static String getCastlingRights (Piece[][] board) {
        StringBuilder castling_rights = new StringBuilder();

        if (board[4][7] != null && board[4][7] instanceof King && !((King)board[4][7]).hasMoved()) {
            if (board[7][7] != null && board[7][7] instanceof Rook && !((Rook)board[7][7]).hasMoved()) {
                castling_rights.append('K');
            }

            if (board[0][7] != null && board[0][7] instanceof Rook && !((Rook)board[0][7]).hasMoved()) {
                castling_rights.append('Q');
            }
        }

        if (board[4][0] != null && board[4][0] instanceof King && !((King)board[4][0]).hasMoved()) {
            if (board[7][0] != null && board[7][0] instanceof Rook && !((Rook)board[7][0]).hasMoved()) {
                castling_rights.append('k');
            }

            if (board[0][0] != null && board[0][0] instanceof Rook && !((Rook)board[0][0]).hasMoved()) {
                castling_rights.append('q');
            }
        }

        if (castling_rights.length() == 0) {
            castling_rights.append('-');
        }

        return castling_rights.toString();
    }

    private static String getEnPassantRights (Piece[][] board) {
        StringBuilder enPassantDestination = new StringBuilder();

        for (int i = 3; i <= 4; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[j][i] != null) {
                    if (board[j][i] instanceof Pawn) {
                        if(((Pawn)board[j][i]).didMoveTwoSpacesLastMove()) {
                            int direction = board[j][i].isWhite() ? 1 : -1;
                            enPassantDestination.append(indexToAlgebraic(j, i + direction));
                            return enPassantDestination.toString();
                        }
                    }
                }
            }
        }

        if (enPassantDestination.length() == 0) {
            enPassantDestination.append('-');
        }

        return enPassantDestination.toString();
    }

    private static String indexToAlgebraic(int x, int y) {
        String algebraic = "";

        algebraic += (char)('a' + x);
        algebraic += (8 - y);

        return algebraic;
    }
}
