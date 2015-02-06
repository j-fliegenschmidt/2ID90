/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group41;

import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 *
 * @author Janis Fliegenschmidt
 */
public class GameNode {
    private DraughtsState state;
    private Move move;
    
    public GameNode(DraughtsState state) {
        this.state = state;
    }
    
    public DraughtsState getState() {
        return this.state;
    }
    
    public void setBestMove(Move move) {
        this.move = move;
    }
    
    public Move getBestMove() {
        return this.move;
    }
}
