package com.undercooked.game.entity.customer;

/**
 * An enum for spawning {@link Customer}s changing which
 * {@link com.undercooked.game.map.Register} that they target.
 */
public enum CustomerTarget {
    /**
     * Target the farthest {@link com.undercooked.game.map.Register} from the {@link Customer}.
     */
    FARTHEST,
    /**
     * Target the closest {@link com.undercooked.game.map.Register} to the {@link Customer}.
     */
    CLOSEST,
    /**
     * Target a random {@link com.undercooked.game.map.Register}.
     */
    RANDOM
}
