/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group41;

import nl.tue.s2id90.draughts.DraughtsState;
/**
 *
 * @author alexau
 */
public class Evaluation extends AlphaBetaPlayer {

    @Override
    public int GetRating(GameNode node) {
       DraughtsState state = node.getState();
       int rating = piececount(state);
       rating += kingDefense(state);
       
       return rating;
    }
    
    private int piececount(DraughtsState state) {
        int rating = 0;
        for (int piece : state.getPieces()) {
            switch (piece) {
                case DraughtsState.BLACKKING:
                    ++rating;
                case DraughtsState.BLACKPIECE:
                    ++rating;
                    break;

                case DraughtsState.WHITEKING:
                    --rating;
                case DraughtsState.WHITEPIECE:
                    --rating;
                    break;
            }
        }
        
        return rating;
    }
    
    private int kingDefense(DraughtsState state) {
        int rating = 0;
        int[] piece = state.getPieces();
        
        for (int i = 1; i < 6; i++) {
            if (piece[i] == DraughtsState.BLACKPIECE) {
                ++rating;
            }
        }
        
        for (int i = 46; i < 51; i++) {
            if (piece[i] == DraughtsState.BLACKPIECE) {
                --rating;
            }
        }
        
        return rating;
    }
    
    
}
