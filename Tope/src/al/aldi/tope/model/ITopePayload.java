package al.aldi.tope.model;

import java.util.HashMap;
import java.util.List;

public interface ITopePayload {
    public HashMap<String, String> getParameters();

    public List<String> getPayloads();


}
