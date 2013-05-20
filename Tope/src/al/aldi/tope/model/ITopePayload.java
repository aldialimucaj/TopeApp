package al.aldi.tope.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * The payload which gets carried on to the server.
 *
 * @author Aldi Alimucaj
 *
 */
public interface ITopePayload extends Serializable {
    public void addPayload(String key, String value) throws Exception;

    public HashMap<String, String> getParameters();

    public List<String> getPayloads();

    public void clear();


}
