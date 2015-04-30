package com.app.crunchyonioncoolkit.elderalarm;

import java.util.ArrayList;

/**
 * Created by David on 2015-04-28.
 */
public class EventListener {

    public static ArrayList<Event> listeners;

    // Add an instance to subscribe to events
    public void addEventListener(Event event) {
        if (listeners == null)
            listeners = new ArrayList<>();

        listeners.add(event);
    }

    public void removeEventListener(Event event) {
        if (listeners != null)
            listeners.remove(event);
    }

    public void fireEvents(Result value) {
        if (listeners != null) {

            for (Event event : listeners) {
                event.onChange(value);
            }
        }
    }

}
