package com.undercooked.game.load;

public enum LoadResult {
    /** Returns if the load was a success. */
    SUCCESS,
    /**
     * Returns if the load successfully went through,
     * but had a few things that couldn't load / went wrong.
     */
    PARTIAL_SUCCESS,
    /**
     * Returns if the load was a total failure.
     * (Only if it could not load at all)
     */
    FAILURE
}
