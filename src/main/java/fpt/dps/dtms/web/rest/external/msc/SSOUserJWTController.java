package fpt.dps.dtms.web.rest.external.msc;

import fpt.dps.dtms.security.jwt.JWTConfigurer;
import fpt.dps.dtms.security.jwt.TokenProvider;
import fpt.dps.dtms.service.LoginTrackingService;
import fpt.dps.dtms.service.UserService;
import fpt.dps.dtms.service.dto.LoginTrackingDTO;
import fpt.dps.dtms.web.rest.vm.LoginVM;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api/external/sso")
public class SSOUserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;
    
    private final UserService userService;
    
    private final LoginTrackingService loginTrackingService;

    public SSOUserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager, UserService userService, LoginTrackingService loginTrackingService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.loginTrackingService = loginTrackingService;
    }

    /**
     * Check authenticate from external site
     * @param loginVM
     * @return
     * @author TuHP
     */
    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        this.userService.updateStatus(loginVM.getUsername(), true);
        LoginTrackingDTO loginTrackingDTO = new LoginTrackingDTO();
        loginTrackingDTO.setLogin(loginVM.getUsername());
        loginTrackingDTO.setStartTime(Instant.now());
        this.loginTrackingService.save(loginTrackingDTO);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt, loginVM.getUsername()), httpHeaders, HttpStatus.OK);
    }
    
	/*
	 * @PostMapping("/authentication")
	 * 
	 * @Timed public Authentication
	 * getAuthentication(@RequestHeader(JWTConfigurer.AUTHORIZATION_HEADER) String
	 * bearerToken) { String token = this.resolveToken(bearerToken); return
	 * tokenProvider.getAuthentication(token); }
	 */
    
    /**
     * Validate token and return authentication for each request from external
     * @param bearerToken
     * @return
     */
    @PostMapping("/authentication")
    @Timed
    public ResponseEntity<SSO> validateToken(@RequestHeader(JWTConfigurer.AUTHORIZATION_HEADER) String bearerToken) {
    	String token = this.resolveToken(bearerToken);
    	HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + token);
        Map<String, Object> map = this.tokenProvider.getAuthentication(token);
        SSO sso = new SSO(tokenProvider.validateToken(token), (String)map.get("authorities"), (String)map.get("subject"));
        return new ResponseEntity<>(sso, httpHeaders, HttpStatus.OK);
    }
    
    private String resolveToken(String bearerToken){
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    /**
     * Object to return as body for Single Sign On.
     */
    static class SSO{
    	private boolean validateToken;
    	
    	private String subject;
    	
    	private String authorities;
    	
    	SSO(boolean validateToken, String authorities, String subject){
    		this.validateToken = validateToken;
    		this.authorities = authorities;
    		this.subject = subject;
    	}

		public boolean isValidateToken() {
			return validateToken;
		}

		public void setValidateToken(boolean validateToken) {
			this.validateToken = validateToken;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getAuthorities() {
			return authorities;
		}

		public void setAuthorities(String authorities) {
			this.authorities = authorities;
		}
    }
    
    static class JWTToken {

        private String idToken;
        
        private String login;

        JWTToken(String idToken, String login) {
            this.idToken = idToken;
            this.login = login;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("login")
		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}
    }
}
