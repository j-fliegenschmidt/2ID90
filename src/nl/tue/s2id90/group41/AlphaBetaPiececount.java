/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group41;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 *
 * @author Janis Fliegenschmidt
 */
public class AlphaBetaPiececount extends AlphaBetaPlayer {
    @Override
    public int GetRating(GameNode node, int side) {
        int rating = 0;
        for (int piece : node.getState().getPieces()) {
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
        
        return rating;
    }
}
