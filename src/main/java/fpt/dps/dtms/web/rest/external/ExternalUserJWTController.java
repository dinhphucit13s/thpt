package fpt.dps.dtms.web.rest.external;

import fpt.dps.dtms.security.jwt.JWTConfigurer;
import fpt.dps.dtms.security.jwt.TokenProvider;
import fpt.dps.dtms.service.LoginTrackingService;
import fpt.dps.dtms.service.UserService;
import fpt.dps.dtms.service.dto.LoginTrackingDTO;
import fpt.dps.dtms.web.rest.vm.LoginVM;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalUserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;
    
    private final UserService userService;
    
    private final LoginTrackingService loginTrackingService;

    public ExternalUserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager, UserService userService, LoginTrackingService loginTrackingService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.loginTrackingService = loginTrackingService;
    }

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

    /**
     * Object to return as body in JWT Authentication.
     */
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
