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
       int rating = 0;
       
       rating += pieceCount(state);
       rating += kingDefense(state);
       rating += midDomination(state);
       rating += enMasse(state) * 0.5;
       
       return rating;
    }
    
    private int pieceCount(DraughtsState state) {
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
    
    private int midDomination(DraughtsState state) {
        int rating = 0;
        int[] piece = state.getPieces();
        
        for (int i = 21; i < 31; i++) {
            //Janis, does this make sense?
            switch (piece[i]) {
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
        
        return rating;
    }
    
    /**
     * Returns a rating based on advancing together, in order to prevent attacks
     * @param state
     * @return integer
     */
    private int enMasse(DraughtsState state) {
        int rating = 0;
        int[] piece = state.getPieces();
        
        //for-loop in order to target middle nodes in the game boards
        for (int i = 0; i < 31; i = i + 10) {
            
            //for-loop for 8 nodes within two rows
            for (int j = i + 7; j < i + 15; j++ ) {
                if (piece[j] == DraughtsState.BLACKPIECE) {
                    //the pieces that can block capture are on nodes -6, -5
                    if (piece[j-6] == DraughtsState.BLACKPIECE) {
                        ++rating;
                    }
                    if (piece[j-5] == DraughtsState.BLACKPIECE) {
                        ++rating;
                    }
                
                } else if (piece[j] == DraughtsState.WHITEPIECE) {
                    //the pieces that can block capture are on nodes +6, +5
                    if (piece[j+6] == DraughtsState.WHITEPIECE) {
                        --rating;
                    }
                    if (piece[j+5] == DraughtsState.WHITEPIECE) {
                        --rating;
                    }
                }
            }   
        }
        
        return rating;
    }
}
