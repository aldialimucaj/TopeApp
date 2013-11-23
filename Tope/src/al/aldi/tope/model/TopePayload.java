package al.aldi.tope.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The payload which is sent to the server. These values
 * are going to be translated as HTTP parameters. Make sure to register
 * the expected payload otherwise custom payloads are going to be rejected.
 *
 * @author Aldi Alimucaj
 *
 */
public class TopePayload implements ITopePayload {

    protected static final long  serialVersionUID      = 3291081158751882587L;

    public static final String PARAM_ACTION_ID       = "actionId";
    public static final String PARAM_USER            = "user";
    public static final String PARAM_PASSWORD        = "password";
    public static final String PARAM_DOMAIN          = "domain";
    public static final String PARAM_METHOD          = "method";

    public static final String PARAM_ACTIVE          = "active";
    public static final String PARAM_OPPOSITE_ACTION = "oppositeAction";

    public static final String PARAM_TIME_TO_WAIT    = "timeToWait";
    public static final String PARAM_TIME_TO_EXEC    = "timeToExecute";

    public static final String PARAM_ARG_0           = "arg0";
    public static final String PARAM_ARG_1           = "arg1";
    public static final String PARAM_ARG_2           = "arg2";
    public static final String PARAM_ARG_3           = "arg3";
    public static final String PARAM_ARG_4           = "arg4";

    private List<String>       payloads              = new ArrayList<String>();
    HashMap<String, String>    params                = new HashMap<String, String>();

    public TopePayload() {
        registerPayloads();
    }

    /**
     * Registering payload options in order to prevent wild combinations
     */
    private void registerPayloads() {
        payloads.add(PARAM_ACTION_ID);
        payloads.add(PARAM_USER);
        payloads.add(PARAM_PASSWORD);
        payloads.add(PARAM_DOMAIN);
        payloads.add(PARAM_METHOD);
        payloads.add(PARAM_ACTIVE);
        payloads.add(PARAM_OPPOSITE_ACTION);
        payloads.add(PARAM_TIME_TO_WAIT);
        payloads.add(PARAM_TIME_TO_EXEC);
        payloads.add(PARAM_ARG_0);
        payloads.add(PARAM_ARG_1);
        payloads.add(PARAM_ARG_2);
        payloads.add(PARAM_ARG_3);
        payloads.add(PARAM_ARG_4);
    }

    /**
     * Adds the key and value to the payload list.
     * It checks if the key exists before otherwise an Exception is thrown.
     *
     */
    public void addPayload(String key, String value) throws Exception {
        if (!payloads.contains(key)) {
            throw new Exception("Payload not supported");
        }
        params.put(key, value);

    }

    @Override
    public HashMap<String, String> getParameters() {
        return params;
    }

    @Override
    public List<String> getPayloads() {
        return payloads;
    }

    @Override
    public void clear() {
        params.clear();
    }
}
