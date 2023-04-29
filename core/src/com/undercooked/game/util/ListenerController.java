package com.undercooked.game.util;

import com.badlogic.gdx.utils.Array;

public class ListenerController<T> {
    Array<Listener<T>> listeners;
    public ListenerController() {
        listeners = new Array<>();
    }

    public void addListener(Listener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<T> listener) {
        listeners.removeValue(listener, true);
    }

    public void tellListeners(T value) {
        for (Listener listener : listeners) {
            listener.tell(value);
        }
    }

}
