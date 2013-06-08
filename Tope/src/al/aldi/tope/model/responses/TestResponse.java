package al.aldi.tope.model.responses;

public class TestResponse implements ITopeResponsePayload {
    String testMessage = null;

    public TestResponse() {

    }

    public String getTestMessage() {
        return testMessage;
    }

    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }
}
