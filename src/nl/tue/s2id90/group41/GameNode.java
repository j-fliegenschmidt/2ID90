/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group41;

import java.util.ArrayList;
import java.util.List;
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
    
    public List<GameNode> getSuccessors() {
        ArrayList<GameNode> result = new ArrayList<>();
        
        state.getMoves().stream().map((mv) -> {
            DraughtsState clone = this.state.clone();
            clone.doMove(mv);
            return clone;
        }).forEach((newState) -> {
            result.add(new GameNode(newState));
        });
        
        return result;
    }
}
