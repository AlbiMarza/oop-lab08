package it.unibo.deathnote.impl;

import java.util.LinkedHashMap;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote{
    
    private final Map<String, Death> deaths;
    private String lastWrittenName;

    public DeathNoteImpl(){
        this.deaths = new LinkedHashMap<String, Death>();
    }

    public String getRule(int ruleNumber){
        if (ruleNumber < 1 || ruleNumber > RULES.size()){
            throw new IllegalArgumentException("Rule index " + ruleNumber + " invalid");
        }
        return RULES.get(ruleNumber - 1);
    }

    public void writeName(String name){
        Objects.requireNonNull(name);
        this.lastWrittenName = name;
        this.deaths.put(name, new Death());
    }

    public boolean writeDeathCause
}
