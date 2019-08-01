/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.security;

import java.security.Principal;
import java.util.Arrays;

/**
 *
 * @author 43700118
 */
public class UserContext implements Principal {

    private Long id;
    private String username;
    private String roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean verificaPermiso(String... tests) {

        if (tests == null) {
            return true;
        }
        
        if(roles == null){
            return false;
        }

        boolean v = false;
        try {
            
            String r = roles.split("[.]")[0];
            System.out.println("ROLE: " + r);
            for(String t:tests){
                if(r.equals(t)){
                    v = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public String getName() {
        return getUsername();
    }

}
