/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kelaskoding.resource;

import com.kelaskoding.entity.User;
import com.kelaskoding.repo.UserRepo;
import com.kelaskoding.utility.KeyGenerator;
import com.kelaskoding.utility.PasswordUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author jarvis
 */
@Path("/users")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Inject
    private UserRepo repo;
    
    @Inject
    private KeyGenerator keyGenerator;
    
    @Context
    private UriInfo uriInfo;
    
    @POST
    public Response register(User user){
        user.setPassword(PasswordUtils.digestPassword(user.getPassword()));
        repo.create(user);
        return Response.ok(user).build();
    }
    
    @GET
    public List<User> findAll(){
        return repo.findAll();
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("email") String email, @FormParam("password") String password){
        Map<String, String> data = new HashMap<>();
        
        try{
            authenticate(email, password);
            String token = issueToken(email);
            data.put("token", token);
            return Response.ok(data).build();
        }catch(Exception ex){
            data.put("message", ex.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(data).build();
        }
    }

    private void authenticate(String email, String password) {
        User user = repo.findByEmailAndPassword(email, PasswordUtils.digestPassword(password));
        if(user == null){
            throw new SecurityException("Invalid email/password");
        }
    }

    private String issueToken(String email) {
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(email)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(10L))) // 1 menit
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return jwtToken;
    }
    
    
    private Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
}
