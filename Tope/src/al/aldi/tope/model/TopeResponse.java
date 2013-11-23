package al.aldi.tope.model;

import java.util.Date;

/**
 * This class represents the response model expected by the server.
 * The response itself has this predefined structure. But it can also have
 * complex objects returns which are represented in JSON format and stored in
 * the payload object.
 * @param <E> The payload object that is being expected from the server to fill
 */
public class TopeResponse<E> {
    private boolean success    = false; // set by the server if the command was successful
    private int     statusCode = 0;     // http status of the call
    private String  message    = null;  // text message in case of error
    private String  command    = null;  // command it executed
    private String  responseId = null;  // unique id
    private String  requestId  = null;  // unique id
    private Date    date       = null;  // response date
    private boolean ignore     = false; // in case the response is obsolete or not awaiting one
    private E       payload    = null;  // dynamic payload, depending on the request

    public TopeResponse() {
    }
    
    public TopeResponse(boolean success) {
        super();
        this.success = success;
    }
    

    public TopeResponse(boolean success, boolean ignore) {
        super();
        this.success = success;
        this.ignore = ignore;
    }


    @Override
    public String toString() {
        return "TopeResponse [success=" + success + ", statusCode=" + statusCode + ", message=" + message + ", command=" + command + ", responseId=" + responseId + ", requestId=" + requestId + ", date=" + date
                + ", payload=" + (payload != null ? payload.getClass() : "null") + "]";
    }

    public E getPayload() {
        return payload;
    }

    public void setPayload(E payload) {
        this.payload = payload;
    }

    public boolean isSuccessful() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }
    

}
