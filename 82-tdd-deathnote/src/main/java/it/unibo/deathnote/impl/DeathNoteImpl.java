package it.unibo.deathnote.impl;

import java.util.LinkedHashMap;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote{
    
    private final Map<String, Death> deaths;
    private String lastWrittenName;

    public DeathNoteImpl() {
        this.deaths = new LinkedHashMap<String, Death>();
    }

    public String getRule(int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > RULES.size()){
            throw new IllegalArgumentException("Rule index " + ruleNumber + " invalid");
        }
        return RULES.get(ruleNumber - 1);
    }

    public void writeName(String name) {
        Objects.requireNonNull(name);
        this.lastWrittenName = name;
        this.deaths.put(name, new Death());
    }

    public boolean writeDeathCause(final String cause) {
        return updateDeath(cause, new DeathTransformer(){
            @Override 
            public Death call(final Death input){
                return input.writeCause(cause);
            }
        });
    }

    public boolean writeDeathDetails(final String details) {
        return updateDeath(details, new DeathTransformer(){
            @Override 
            public Death call(final Death input){
                return input.writeCause(details);
            }
        });
    }

    public String getDeathCause(final String name) {
        return getDeath(name).cause;
    }

    public String getDeathDetails(final String name) {
        return getDeath(name).details;
    }

    public Death getDeath(final String name) {
        final var death = deaths.get(name);
        if (death == null) {
            throw new IllegalArgumentException(name + " has never been written on this book.");
        }
        return death;
    }


    public boolean updateDeath(final String update, final DeathTransformer operation){
        if (lastWrittenName == null) {
            throw new IllegalStateException("No name written yet");
        }
        if (update == null) {
            throw new IllegalArgumentException("No update given");
        }
        final var previous = deaths.get(lastWrittenName);
        final var updated = operation.call(previous);
        if (previous.equals(updated)) {
            return false;
        } else {
            deaths.put(lastWrittenName, updated);
            return true;
        }
    }

    @FunctionalInterface
    public interface DeathTransformer {
        Death call(Death input);
    }

    private static final class Death {
        private static final String DEFAULT_CAUSE = "heart attack";
        private static final byte CAUSE_TIMELIMIT = 40;
        private static final short DETAILS_TIMELIMIT = 600 + CAUSE_TIMELIMIT;
        private final String cause;
        private final String details;
        private final long timeOfDeath;

        private int hash;

        private Death(final String cause, final String details) {
            this.cause = cause;
            this.details = details;
            timeOfDeath = System.currentTimeMillis();
        }

        Death() {
            this.(DEFAULT_CAUSE, "");
        }

        Death writeCause(final String cause) {
            return System.currentTimeMillis() < timeOfDeath + CAUSE_TIMELIMIT
                ? new Death(cause, this.details)
                : new Death();
        }

        Death writeCause(final String details) {
            return System.currentTimeMillis() < timeOfDeath + DETAILS_TIMELIMIT
                ? new Death(this.cause, details)
                : new Death();
        }

        @Override 
        public boolean equals(final Object obj){
            if (this == obj) {
                return true;
            }
        }

        @Override 
        public int hashCode() {
            if (hash == 0) {
                return Objects.hash(cause, details, timeOfDeath);
            }
        }
    }
}
