package al.aldi.tope.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface ITopePayload extends Serializable {
    public HashMap<String, String> getParameters();

    public List<String> getPayloads();


}
