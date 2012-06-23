package com.shastram.web8085.client.pattern;

import java.util.ArrayList;
import java.util.HashMap;

public class SignalSlot extends Observable {

    public static enum Signals {
        EXAMPLE_SOURCE_CODE_AVAILABLE
    }

    private HashMap<Signals, ArrayList<Observer>> observerMap = new HashMap<SignalSlot.Signals, ArrayList<Observer>>();

    public static class SignalData {
        public Signals signal;
        public HashMap<String, Object> mapData = new HashMap<String, Object>();
        public Object singleData;

        public SignalData(Signals signal, HashMap<String, Object> mapData) {
            this.signal = signal;
            this.mapData = mapData;
        }

        public SignalData(Signals signal, Object singleData) {
            this.signal = signal;
            this.singleData = singleData;
        }
    }

    // This static singleton is converted to JS, so there are no MT issues
    public static SignalSlot instance = new SignalSlot();

    public void notiyAbout(Signals signal, Object data) {
        SignalData signalData = new SignalData(signal, data);
        notifyObservers(signalData);
    }

    public void notifyAbout(Signals signal, Object... keyValues) {
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        for (int i = 0; i < keyValues.length;) {
            String key = (String) keyValues[i];
            ++i;
            if (i < keyValues.length) {
                dataMap.put(key, keyValues[i]);
            }
            ++i;
        }
        SignalData signalData = new SignalData(signal, dataMap);
        notifyObservers(signalData);
    }

    private void notifyObservers(SignalData signalData) {
        ArrayList<Observer> observers = observerMap.get(signalData.signal);
        if (observers != null) {
            for (Observer o : observers) {
                o.update(signalData);
            }
        }
    }

    public void addObserver(Signals signal, Observer observer) {
        ArrayList<Observer> observers = observerMap.get(signal);
        if (observers == null) {
            observers = new ArrayList<Observer>();
            observerMap.put(signal, observers);
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
}
