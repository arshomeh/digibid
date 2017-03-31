package gr.uoa.di.digibid.model;

import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

import lombok.ToString;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@ToString
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private WebUser webUser;

    public CurrentUser(WebUser webUser) {
        super(webUser.getUsername(), webUser.getPassword(), AuthorityUtils.createAuthorityList(webUser.getRoles().stream().toArray(String[]::new)));
        this.webUser = webUser;
    }

    public WebUser getUser() {
        return webUser;
    }

    public String getFullName() {
        return webUser.getFirstName() + " " + webUser.getLastName();
    }

    public List<String> getRoles() {
        return webUser.getRoles();
    }
}