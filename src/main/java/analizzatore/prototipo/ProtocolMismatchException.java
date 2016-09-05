package analizzatore.prototipo;

/**
 * Created by Antonio on 18/08/2016.
 * Gestisce l'eccezione dove il protocollo del pacchetto non corrisponde a quello della traccia in esame.
 */
public class ProtocolMismatchException extends Exception {
    public ProtocolMismatchException(String message){
        super(message);
    }
}