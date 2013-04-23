package al.aldi.tope.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopePayload implements ITopePayload {

    private static final long	serialVersionUID	= 3291081158751882587L;

    public static final String PARAM_TIME_TO_WAIT = "timeToWait";
    public static final String PARAM_TIME_TO_EXEC = "timeToExecute";

    private List<String> payloads = new ArrayList<String>();
    HashMap<String, String> params = new HashMap<String, String>();

    public TopePayload() {
        registerPayloads();
    }

    private void registerPayloads() {
         payloads.add(PARAM_TIME_TO_WAIT);
         payloads.add(PARAM_TIME_TO_EXEC);
    }

    public void addPayload(String key, String value) throws Exception{
        if(!payloads.contains(key)){
            throw new Exception("Payload not supported");
        }
        params.put(key, value);

    }

    @Override
    public HashMap<String,String> getParameters() {
        return params;
    }

    @Override
    public List<String> getPayloads(){
        return payloads;
    }
}
