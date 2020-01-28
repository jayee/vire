package se.vire.record;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/record")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordResource {

    @Inject
    RecordService recordService;

    @GET
    @Path("start")
    public Response start() {
        String status = recordService.startRecord();
        return Response.ok(toJsonStatus(status)).build();
    }

    @GET
    @Path("stop")
    public Response stop() {
        String status = recordService.stopRecord();
        return Response.ok(toJsonStatus(status)).build();
    }

    private String toJsonStatus(String status) {
        return String.format("{'status':'%s'}", status);
    }
}