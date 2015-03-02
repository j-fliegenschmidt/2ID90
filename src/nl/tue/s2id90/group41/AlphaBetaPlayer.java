/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group41;

import java.util.logging.Level;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;
import java.util.logging.Logger;

/**
 * @author Janis Fliegenschmidt
 */
public abstract class AlphaBetaPlayer extends DraughtsPlayer {

    private static final Logger LOG = Logger.getLogger(AlphaBetaPlayer.class.getName());

    private int value;
    private boolean stopped;

    public AlphaBetaPlayer() {
        super(UninformedPlayer.class.getResource("resources/optimist.png"));
    }

    @Override
    public Move getMove(DraughtsState draughtsState) {
        this.stopped = false;
        return AlphaBeta(draughtsState);
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void stop() {
        this.stopped = true;
    }

    private Move AlphaBeta(DraughtsState state) {
        GameNode node = new GameNode(state);
        node.setBestMove(node.getState().getMoves().get(0));

        Move move = null;
        int tempValue = Integer.MIN_VALUE;
        int depth = 0;
        do {
            move = node.getBestMove();
            this.value = tempValue;
            tempValue = AlphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, depth++);
        } while (!this.IsStopped());

        LOG.log(Level.INFO, "Search depth achieved: {0}", --depth);

        return move;
    }

    private int AlphaBeta(GameNode node, int alpha, int beta, int depth) {
        if (node.getState().isWhiteToMove()) {
            for (Move move : node.getState().getMoves()) {
                node.getState().doMove(move);

                int tmp = AlphaBetaMax(node, alpha, beta, depth);
                if (tmp < beta) {
                    beta = tmp;
                    node.setBestMove(move);
                }

                node.getState().undoMove(move);
            }

            return beta;
        } else {
            for (Move move : node.getState().getMoves()) {
                node.getState().doMove(move);

                int tmp = AlphaBetaMin(node, alpha, beta, depth);
                if (tmp > alpha) {
                    alpha = tmp;
                    node.setBestMove(move);
                }

                node.getState().undoMove(move);
            }

            return alpha;
        }
    }

    private int AlphaBetaMax(GameNode node, int alpha, int beta, int depth) {
        if (depth <= 0 || node.getState().isEndState() || this.IsStopped()) {
            return GetRating(node);
        }

        for (Move move : node.getState().getMoves()) {
            node.getState().doMove(move);

            int tmp = AlphaBetaMin(node, alpha, beta, --depth);
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
        if (depth <= 0 || node.getState().isEndState() || this.IsStopped()) {
            return GetRating(node);
        }

        for (Move move : node.getState().getMoves()) {
            node.getState().doMove(move);

            int tmp = AlphaBetaMax(node, alpha, beta, --depth);
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

    protected boolean IsStopped() {
        return this.stopped;
    }
}
