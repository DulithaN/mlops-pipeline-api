package reassessment.csa.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GlobalSafetyNetExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOGGER = Logger.getLogger(GlobalSafetyNetExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable errorTrack) {
        // Log critical trace detail internally for engineer diagnostics
        LOGGER.log(Level.SEVERE, "Internal server anomaly intercepted by safety net: ", errorTrack);

        // Return generic message masking raw stack details from external exposure
        ErrorResponseSchema error = new ErrorResponseSchema(
                "INTERNAL_SERVER_ERROR", 
                "An unexpected error occurred. Please contact administrative maintenance teams.", 
                500
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}