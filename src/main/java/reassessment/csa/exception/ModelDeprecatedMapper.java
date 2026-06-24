package reassessment.csa.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ModelDeprecatedMapper implements ExceptionMapper<ModelDeprecatedException> {
    @Override
    public Response toResponse(ModelDeprecatedException ex) {
        ErrorResponseSchema error = new ErrorResponseSchema("STATE_VIOLATION_FORBIDDEN", ex.getMessage(), 403);
        return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(error).build();
    }
}