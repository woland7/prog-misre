package analizzatore.prototipo;

/**
 * Created by Antonio on 20/08/2016.
 * Gestisce il caso in cui la transizione non fa parte di quelle del protocollo in esame.
 */
public class TransitionNotFoundException extends Exception {
    public TransitionNotFoundException(String message){
        super(message);
    }
}
