package ua.kpi.tef.zu.gp3servlet.controller.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Anton Domin on 2020-04-17
 */
public class SessionListener implements HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		UserSecurity.removeLoggedUser(httpSessionEvent.getSession());
	}
}
