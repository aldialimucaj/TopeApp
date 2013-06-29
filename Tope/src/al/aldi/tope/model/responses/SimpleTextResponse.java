package al.aldi.tope.model.responses;

public class SimpleTextResponse implements ITopeResponsePayload {
    String message = null;

    public SimpleTextResponse() {

    }

    public String getTestMessage() {
        return message;
    }

    public void setTestMessage(String testMessage) {
        this.message = testMessage;
    }
}
