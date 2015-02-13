/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group41;

import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;

/**
 * TODO: Store move only in outermost alphabeta call
 *       Return ratings
 *       side Factor updated per alphabetacall
 * @author Janis Fliegenschmidt
 */
public class AlphaBetaPlayer extends DraughtsPlayer {

    private int value;

    public AlphaBetaPlayer() {
        super(UninformedPlayer.class.getResource("resources/optimist.png"));
    }

    @Override
    public Move getMove(DraughtsState draughtsState) {
        return AlphaBeta(draughtsState);
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
    
    private int sideFactor = 1; // 1= Black, -1 = white

    private Move AlphaBeta(DraughtsState state) {
        if (state.isWhiteToMove()) {
            this.sideFactor = -1;
        }
        
        GameNode node = new GameNode(state);
        AlphaBetaMax(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        return node.getBestMove();
    }

    private int alphaBetaDepth = 5;

    private int AlphaBetaMax(GameNode node, int alpha, int beta, int depth) {
        if (depth >= alphaBetaDepth) {
            return GetRating(node);
        }

        for (Move move : node.getState().getMoves()) {
            node.getState().doMove(move);
            
            int tmp = AlphaBetaMin(node, alpha, beta, ++depth);
            if (tmp > alpha) {
                alpha = tmp;
                node.setBestMove(move);
            }
            
            node.getState().undoMove(move);

            if (alpha >= beta) {
                return beta;
            }
        }

        return alpha;
    }

    private int AlphaBetaMin(GameNode node, int alpha, int beta, int depth) {
        if (depth >= alphaBetaDepth) {
            return GetRating(node);
        }

        for (Move move : node.getState().getMoves()) {
            node.getState().doMove(move);

            int tmp = AlphaBetaMax(node, alpha, beta, ++depth);
            if (tmp < beta) {
                beta = tmp;
                node.setBestMove(move);
            }
            
            beta = tmp > beta ? beta : tmp;

            node.getState().undoMove(move);

            if (beta >= alpha) {
                return alpha;
            }
        }

        return beta;
    }

    private int GetRating(GameNode node) {
        int rating = 0;
        for (int piece : node.getState().getPieces()) {
            switch (piece) {
                case DraughtsState.BLACKKING:
                case DraughtsState.BLACKPIECE:
                    ++rating;
                    break;

                case DraughtsState.WHITEKING:
                case DraughtsState.WHITEPIECE:
                    --rating;
                    break;
            }
        }
        
        return rating * this.sideFactor;
    }
}
