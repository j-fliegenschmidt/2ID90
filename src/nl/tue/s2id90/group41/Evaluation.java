/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group41;

import java.util.Arrays;
import java.util.List;
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
       
       rating += pieceCount(state) * 5; // this is the most important thing to keep an eye out for
       rating += kingDefense(state) * 7; // this can hardly be overrated, and will rarely conflict
                                         // the piececount
       rating += midDomination(state); // this is great, but it should never 
                                       // lead to our player letting its pieces 
                                       // get force captured to keep the enemy 
                                       // out of the middle
       rating += enMasse(state) * 2; // This needs to have higher rating than mid domination,
                                     // for similar reasons. You can't dominate mid wihout backup
       
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
        
        //pattern for the TL, TR, BR, BL for 7,8,9,10 etc
        List<Integer> pattern1 = Arrays.asList(-6,-5,5,4);
        
        //pattern for the TL, TR, BR, BL for 7,8,9,10 etc
        List<Integer> pattern2 = Arrays.asList(-5,-4,6,5);
        
        //excludes border nodes, focus on center nodes
        for (int i = 0; i < 31; i = i + 10) {
            
            //first four nodes
            for (int j = i + 7; j < i + 11; j++) {
                int foo = piece[j];
                if (evalBlackPiece(foo)) {
                    rating += enMasseBlack(pattern1, foo, piece);
                } else if (evalWhitePiece(foo)) {
                    rating -= enMasseWhite(pattern1, foo, piece);
                }
            }
            
            //second four nodes
            for (int k = i + 11; k < i + 15; k++) {
                int bar = piece[k];
                if (evalBlackPiece(bar)) {
                    rating += enMasseBlack(pattern2, bar, piece);
                } else if (evalWhitePiece(bar)) {
                    rating -= enMasseWhite(pattern2, bar, piece);
                }
            }      
        }
        
        return rating;
    }
    
    private int enMasseBlack(List<Integer> pattern, int abc, int[] piece) {
        int rating = 0;
        
        //determines the top left, top right, bottom right, and bottom left
        int TL = piece[abc + pattern.get(0)];
        int TR = piece[abc + pattern.get(1)];
        int BR = piece[abc + pattern.get(2)];
        int BL = piece[abc + pattern.get(3)];
        
        //determines if top left has enemy king and whether it can be blocked
        if (evalWhiteKing(TL)) {
            if (evalBlackPiece(BR)) {
                rating += 2;
            }    
        }
        
        //determines if top right has enemy king and whether it can be blocked
        if (evalWhiteKing(TR)) {
            if (evalBlackPiece(BL)) {
                rating += 2;
            }
        }
        
        //determines if top left has black piece or king and whether it has blocked a capture
        if (evalBlackPiece(TL)) {
            if (evalWhitePiece(BR)) {
                rating += 2;
            } else {
                ++rating;
            }
        }
        
        //determines if top right has black piece or king and whether it has blocked a capture
        if (evalBlackPiece(TR)) {
            if (evalWhitePiece(BL)) {
                rating += 2;
            } else {
                ++rating;
            }
        }
        
        return rating;
    }
    
    private int enMasseWhite(List<Integer> pattern, int abc, int[] piece) {
        int rating = 0;
        
        int TL = piece[abc + pattern.get(0)];
        int TR = piece[abc + pattern.get(1)];
        int BR = piece[abc + pattern.get(2)];
        int BL = piece[abc + pattern.get(3)];
        
        if (evalBlackKing(BL)) {
            if (evalWhitePiece(TR)) {
                rating += 2;
            }    
        }
        
        if (evalBlackKing(BR)) {
            if (evalWhitePiece(TL)) {
                rating += 2;
            }
        }
        
        if (evalWhitePiece(BL)) {
            if (evalBlackPiece(TR)) {
                rating += 2;
            } else {
                ++rating;
            }
        }
        
        if (evalWhitePiece(BR)) {
            if (evalBlackPiece(TL)) {
                rating += 2;
            } else {
                ++rating;
            }
        }
        
        return rating;
    }
    
    private boolean evalBlackPiece(int x) {
        return (x == DraughtsState.BLACKPIECE || x == DraughtsState.BLACKKING);
    }
    
    private boolean evalBlackKing(int x) {
        return (x == DraughtsState.BLACKKING);
    }
    
    private boolean evalWhitePiece(int x) {
        return (x == DraughtsState.WHITEPIECE || x == DraughtsState.WHITEKING);
    }
    
    private boolean evalWhiteKing(int x) {
        return (x == DraughtsState.WHITEKING);
    }
    
}
