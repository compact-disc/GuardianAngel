package com.guardianangel.security;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 10/25/2019
 * 
 * This handles tokens for the user and stores them for use as well.
 * Mostly holds data for logged in twitter users. TwitterID is stored here and used in most classes.
 */

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.*;

public class TokenAuthentication extends AbstractAuthenticationToken {

	/**
	 * The JSON Web Token for auth0
	 */
    private final DecodedJWT jwt;
    
    /**
     * Check for validity 
     */
    private boolean invalidated;

    /**
     * Used for setting the JSON web token
     * 
     * @param jwt JSON web token to be set and used
     */
    public TokenAuthentication(DecodedJWT jwt) {
        super(readAuthorities(jwt));
        this.jwt = jwt;
    }

    /**
     * Check if token has expired
     * 
     * @return boolean for validity
     */
    private boolean hasExpired() {
        return jwt.getExpiresAt().before(new Date());
    }

    /**
     * Read the granted login authorities
     * 
     * @param jwt JSON web token from login
     * @return authorities (permissions) for login
     */
    private static Collection<? extends GrantedAuthority> readAuthorities(DecodedJWT jwt) {
        Claim rolesClaim = jwt.getClaim("https://access.control/roles");
        if (rolesClaim.isNull()) {
            return Collections.emptyList();
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] scopes = rolesClaim.asArray(String.class);
        for (String s : scopes) {
            SimpleGrantedAuthority a = new SimpleGrantedAuthority(s);
            if (!authorities.contains(a)) {
                authorities.add(a);
            }
        }
        return authorities;
    }


    /**
     * Get the credentials (JSON web token)
     * 
     * @return the JSON web token
     */
    @Override
    public String getCredentials() {
        return jwt.getToken();
    }

    /**
     * Get the principle
     * 
     * @return return the subject of the JSON web token
     */
    @Override
    public Object getPrincipal() {
        return jwt.getSubject();
    }

    /**
     * Set the authenticated user if it is true
     * 
     * @param authenticated
     */
    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Create a new Authentication object to authenticate");
        }
        invalidated = true;
    }

    /**
     * Check if the user is authenticated
     * 
     * @return true or false based on authentication
     */
    @Override
    public boolean isAuthenticated() {
        return !invalidated && !hasExpired();
    }

    /**
     * Gets the claims for this JWT token.
     * <br>
     * For an ID token, claims represent user profile information such as the user's name, profile, picture, etc.
     * <br>
     * @see <a href="https://auth0.com/docs/tokens/id-token">ID Token Documentation</a>
     * @return a Map containing the claims of the token.
     */
    public Map<String, Claim> getClaims() {
        return jwt.getClaims();
    }

}
