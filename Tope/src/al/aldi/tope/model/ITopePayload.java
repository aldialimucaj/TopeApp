package al.aldi.tope.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * The payload which gets carried on to the server. The payload is translated to HTTP parameters.
 *
 * @author Aldi Alimucaj
 *
 */
public interface ITopePayload extends Serializable {
    /**
     * Add parameters to the payload. See {@link TopePayload} for the
     * predefined parameters
     *
     * @param key
     * @param value
     * @throws Exception
     */
    public void addPayload(String key, String value) throws Exception;

    public HashMap<String, String> getParameters();

    public List<String> getPayloads();

    public void clear();


}
