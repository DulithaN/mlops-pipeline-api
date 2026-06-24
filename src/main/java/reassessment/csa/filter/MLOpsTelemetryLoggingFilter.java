package reassessment.csa.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class MLOpsTelemetryLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOGGER = Logger.getLogger(MLOpsTelemetryLoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty("requestStartTime", System.currentTimeMillis());
        LOGGER.info(String.format("📡 >>> [INBOUND] Request Routed: Method=[%s] | URI=[%s]",
                requestContext.getMethod(), requestContext.getUriInfo().getRequestUri()));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Long startTime = (Long) requestContext.getProperty("requestStartTime");
        long latency = (startTime != null) ? (System.currentTimeMillis() - startTime) : 0;
        
        LOGGER.info(String.format("🏁 <<< [OUTBOUND] Response Dispatched: Method=[%s] | URI=[%s] -> HTTP_STATUS=[%d] | LATENCY=[%dms]",
                requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(),
                responseContext.getStatus(), latency));
    }
}