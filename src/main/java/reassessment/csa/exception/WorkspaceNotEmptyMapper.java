package reassessment.csa.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WorkspaceNotEmptyMapper implements ExceptionMapper<WorkspaceNotEmptyException> {
    @Override
    public Response toResponse(WorkspaceNotEmptyException ex) {
        ErrorResponseSchema error = new ErrorResponseSchema("RESOURCE_CONFLICT", ex.getMessage(), 409);
        return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(error).build();
    }
}