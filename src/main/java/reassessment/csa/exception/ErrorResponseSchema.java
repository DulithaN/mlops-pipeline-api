package reassessment.csa.exception;

public class ErrorResponseSchema {
    private String errorCode;
    private String reason;
    private int httpStatusCode;
    private long logTimestamp;

    public ErrorResponseSchema(String errorCode, String reason, int httpStatusCode) {
        this.errorCode = errorCode;
        this.reason = reason;
        this.httpStatusCode = httpStatusCode;
        this.logTimestamp = System.currentTimeMillis();
    }

    public String getErrorCode() { return errorCode; }
    public String getReason() { return reason; }
    public int getHttpStatusCode() { return httpStatusCode; }
    public long getLogTimestamp() { return logTimestamp; }
}