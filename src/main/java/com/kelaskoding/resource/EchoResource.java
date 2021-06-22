/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kelaskoding.resource;
import com.kelaskoding.filter.JWTTokenNeeded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.Response;

/**
 *
 * @author jarvis
 */
@Path("/echo")
@Produces(TEXT_PLAIN)
public class EchoResource {
    
    
    @GET
    public Response echo(@QueryParam("message") String message){
        return Response.ok().entity(message == null ? "no message": message).build();
    }
    
    @GET
    @Path("/jwt")
    @JWTTokenNeeded
    public Response secureEcho(@QueryParam("message") String message){
        return Response.ok().entity(message == null ? "no message": message).build();
    }
}
