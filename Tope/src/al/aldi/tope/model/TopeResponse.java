package al.aldi.tope.model;

import java.util.Date;

public class TopeResponse<E> {
    private boolean success    = false;
    private int     statusCode = 0;
    private String  message    = null;
    private String  command    = null;
    private String  responseId = null;
    private String  requestId  = null;
    private Date    date       = null;
    private E       payload   = null;

    public TopeResponse() {
    }


    @Override
    public String toString() {
        return "TopeResponse [success=" + success + ", statusCode=" + statusCode + ", message=" + message + ", command=" + command + ", responseId=" + responseId + ", requestId="
                + requestId + ", date=" + date + ", payload=" + (payload!=null?payload.getClass():"null") + "]";
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

}
