package nl.tue.s2id90.group41;


import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;

/**
 * A simple draughts player that plays the first moves that comes to mind
 * and values all moves with value 0.
 * @author huub
 */
public class FirstAttemptPlayer extends DraughtsPlayer {
    private int value;

    public FirstAttemptPlayer() {
        super(UninformedPlayer.class.getResource("resources/optimist.png"));
    }
    
    @Override
    /** @return a random move **/
    public Move getMove(DraughtsState draughtsState) {
        List<Move> moves = draughtsState.getMoves();
        
        int side = draughtsState.isWhiteToMove() ? -1 : 1;
        
        RatedMove ratedMove = null;
        for (Move move : moves) {
            draughtsState.doMove(move);
            
            // rating-algo:
            int rating = 0;
            for (int piece : draughtsState.getPieces()) {
                switch (piece) {
                    case DraughtsState.BLACKKING:
                    case DraughtsState.BLACKPIECE:
                        rating += side;
                        break;
                        
                    case DraughtsState.WHITEKING:
                    case DraughtsState.WHITEPIECE:
                        rating -= side;
                        break;
                }
            }
            
            draughtsState.undoMove(move);
            if (ratedMove == null || ratedMove.getRating() < rating) {
                ratedMove = new RatedMove(move, rating);
            }
        }
        
        this.value = ratedMove == null ? Integer.MIN_VALUE :  ratedMove.getRating();
        return ratedMove == null ? null :  ratedMove.getMove();
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
    
    private class RatedMove {
        private final Move move;
        private final int rating;

        public RatedMove(Move move, int rating) {
            this.move = move;
            this.rating = rating;
        }
        
        public Move getMove() {
            return move;
        }

        public int getRating() {
            return rating;
        }
    }
}
