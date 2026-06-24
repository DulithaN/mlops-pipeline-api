package reassessment.csa.exception;

import javax.ws.rs.WebApplicationException;
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
        // [NEW] Let standard JAX-RS routing errors (like 404 Not Found) pass through normally
        if (errorTrack instanceof WebApplicationException) {
            return ((WebApplicationException) errorTrack).getResponse();
        }

        // Log critical trace detail internally for engineer diagnostics
        LOGGER.log(Level.SEVERE, "Internal server anomaly intercepted by safety net: ", errorTrack);

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