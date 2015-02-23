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
 * Call Min/Max first depending on color. (TODO)
 *
 * @author Janis Fliegenschmidt
 */
public abstract class AlphaBetaPlayer extends DraughtsPlayer {

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

    private Move AlphaBeta(DraughtsState state) {
        GameNode node = new GameNode(state);
        this.value = AlphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return node.getBestMove();
    }

    private final int alphaBetaDepth = 10;

    private int AlphaBeta(GameNode node, int alpha, int beta) {
        if (node.getState().isWhiteToMove()) {
            for (Move move : node.getState().getMoves()) {
                node.getState().doMove(move);

                int tmp = AlphaBetaMin(node, alpha, beta, 0);
                if (tmp > alpha) {
                    alpha = tmp;
                    node.setBestMove(move);
                }

                node.getState().undoMove(move);
            }

            return alpha;
        } else {
            for (Move move : node.getState().getMoves()) {
                node.getState().doMove(move);

                int tmp = AlphaBetaMax(node, alpha, beta, 0);
                if (tmp < beta) {
                    beta = tmp;
                    node.setBestMove(move);
                }

                node.getState().undoMove(move);
            }

            return beta;
        }
    }

    private int AlphaBetaMax(GameNode node, int alpha, int beta, int depth) {
        if (depth >= this.alphaBetaDepth || node.getState().isEndState()) {
            return GetRating(node);
        }

        for (Move move : node.getState().getMoves()) {
            node.getState().doMove(move);

            int tmp = AlphaBetaMin(node, alpha, beta, ++depth);
            if (tmp > alpha) {
                alpha = tmp;
            }

            node.getState().undoMove(move);

            if (alpha >= beta) {
                return beta;
            }
        }

        return alpha;
    }

    private int AlphaBetaMin(GameNode node, int alpha, int beta, int depth) {
        if (depth >= this.alphaBetaDepth || node.getState().isEndState()) {
            return GetRating(node);
        }

        for (Move move : node.getState().getMoves()) {
            node.getState().doMove(move);

            int tmp = AlphaBetaMax(node, alpha, beta, ++depth);
            if (tmp < beta) {
                beta = tmp;
            }

            node.getState().undoMove(move);

            if (beta <= alpha) {
                return alpha;
            }
        }

        return beta;
    }

    public abstract int GetRating(GameNode node);
}
