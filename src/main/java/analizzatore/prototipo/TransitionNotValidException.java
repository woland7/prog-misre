package analizzatore.prototipo;

import org.apache.commons.scxml.model.Transition;

import java.util.List;

/**
 * Created by Antonio on 15/08/2016.
 */
public class TransitionNotValidException extends Exception {
    private List<Transition> transitions;

    public TransitionNotValidException(String message, List<Transition> transitions){
        super(message);
        this.transitions = transitions;
    }

    public List<Transition> getTransitions(){
        return transitions;
    }
}
