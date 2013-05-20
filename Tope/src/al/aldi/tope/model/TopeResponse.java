package al.aldi.tope.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import al.aldi.andorid.filehandling.JSONUtils;

/**
 * Representation of the server response.
 *
 * @author Aldi Alimucaj
 *
 */
public class TopeResponse {

    public static final String				JSON_RES_SUCCESS		= "success";
    public static final String				JSON_RES_STATUS_CODE	= "statusCode";
    public static final String				JSON_RES_MESSAGE		= "message";
    public static final String				JSON_RES_COMMAND		= "command";
    public static final String				JSON_RES_ID				= "responseId";
    public static final String				JSON_REQ_ID				= "requestId";
    public static final String				JSON_RES_DATE			= "date";
    /* * * * */
    public static final String				STR_TRUE				= "true";
    public static final String				STR_FALSE				= "false";
    public static final String				DATE_FORMAT_FULL		= "yyyy-MM-dd HH:mm:ss";

    public static final SimpleDateFormat	sdf						= new SimpleDateFormat(DATE_FORMAT_FULL, Locale.GERMANY);

    private boolean							successful;
    private String							statusCode;
    private String							command;
    private String							message;
    private String							requestId;
    private String							responseId;
    private Date							date;

    public TopeResponse() {
        this.successful = false;
    }

    public TopeResponse(JSONObject jo) {
        JSONUtils ju = new JSONUtils(jo);
        try {
            this.successful = ju.readAttr(JSON_RES_SUCCESS).equals(STR_TRUE);
            this.statusCode = ju.readAttr(JSON_RES_STATUS_CODE);
            this.command = ju.readAttr(JSON_RES_COMMAND);
            this.message = ju.readAttr(JSON_RES_MESSAGE);
            this.requestId = ju.readAttr(JSON_REQ_ID);
            this.responseId = ju.readAttr(JSON_RES_ID);
            this.date = sdf.parse(ju.readAttr(JSON_RES_DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public TopeResponse(boolean successful, String statusCode, String command, String message, String requestId, String responseId, Date date) {
        this.successful = successful;
        this.statusCode = statusCode;
        this.command = command;
        this.message = message;
        this.requestId = requestId;
        this.responseId = responseId;
        this.date = date;
    }

    /**
     * Formats a date into a String
     *
     * @param date
     * @return formated date
     */
    public static String formatDate(Date date) {
        return sdf.format(date);
    }

    /**
     * Parses a date out of a String
     *
     * @param date
     * @return date or null
     */
    public static Date formatString(String date) {

        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        return responseId + " - " + successful + " - " + date;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean success) {
        this.successful = success;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
