package analizzatore.prototipo.model;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Antonio on 19/08/2016.
 */
public class Result {
    private StringBuilder resultStringBuilder;
    private String protocol;
    private String fileName;
    private int countPackets;
    private StringBuilder textualInformation;
    private HashMap<String, Integer> states;

    public Result(){
        this.resultStringBuilder = new StringBuilder("Analisi...:\n");
        this.textualInformation = new StringBuilder("Sintesi del percorso:\n");
        states = new HashMap<String,Integer>();
    }

    public HashMap<String, Integer> getNumberStates(){
        return this.states;
    }

    public String getResultString(){
        this.resultStringBuilder.append(textualInformation);
        return this.resultStringBuilder.toString();
    }

    public void setProtocol(String protocol){
        this.protocol = protocol;
        this.resultStringBuilder.append("Tipo di protocollo: " + protocol);
        this.resultStringBuilder.append("\n");
    }

    public void setFileName(String fileName){
        this.resultStringBuilder.append("File analizzato: " + fileName);
        this.resultStringBuilder.append("\n");
    }

    public void setCountPackets(int countPackets){
        this.countPackets = countPackets;
        this.resultStringBuilder.append("Numero di pacchetti: " + countPackets);
        this.resultStringBuilder.append("\n");
    }

    private void incrementNumberStates(String key){
        states.replace(key, states.get(key) + 1);
    }

    public void addEvent(String event){
        this.textualInformation.append("Event: " + event + "\n");
    }

    public void addState(String state){
        this.textualInformation.append("State: " + state + "\n");
        incrementNumberStates(state);
    }

    public void addExtraInfo(String info){
        this.textualInformation.append("\t: " + info + "\n");
    }

    public void addNumberStatesInfo(){
        this.textualInformation.append("Frequenza degli stati:\n");
        Set<String> keySet = states.keySet();
        for(String s: keySet)
            this.textualInformation.append(s + ": " + states.get(s) + "\n");
    }
}