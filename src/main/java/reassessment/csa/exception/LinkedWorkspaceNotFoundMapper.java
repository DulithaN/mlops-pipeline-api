package reassessment.csa.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedWorkspaceNotFoundMapper implements ExceptionMapper<LinkedWorkspaceNotFoundException> {
    @Override
    public Response toResponse(LinkedWorkspaceNotFoundException ex) {
        // Mapping directly to 422 Unprocessable Entity to reflect semantic correctness
        ErrorResponseSchema error = new ErrorResponseSchema("UNPROCESSABLE_ENTITY_DEPENDENCY", ex.getMessage(), 422);
        return Response.status(422).type(MediaType.APPLICATION_JSON).entity(error).build();
    }
}