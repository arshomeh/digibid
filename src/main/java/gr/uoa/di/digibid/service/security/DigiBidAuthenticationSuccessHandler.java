package gr.uoa.di.digibid.service.security;

import gr.uoa.di.digibid.model.CurrentUser;
import gr.uoa.di.digibid.persist.domain.User;
import gr.uoa.di.digibid.web.controller.LoginController;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amehrabyan, gpozidis on 30/08/16.
 */
@Controller
public class DigiBidAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        handle(httpServletRequest, httpServletResponse, authentication);
    }

    protected void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, determineTargetUrl(authentication));
    }

    /**
     * Builds the target URL according to the logic defined in the main class Javadoc.
     */
    protected String determineTargetUrl(Authentication authentication) {
        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

        return currentUser != null && currentUser.getRoles().contains(User.Role.ADMIN.toString()) ? LoginController.ADMIN_SUCCESS_URL : LoginController.DEFAULT_SUCCESS_URL;
    }
}