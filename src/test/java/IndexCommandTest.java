import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.kpi.tef.zu.gp3servlet.controller.Servlet;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Anton Domin on 2020-07-04
 */
public class IndexCommandTest {
	private final Servlet servlet = new Servlet();
	@Mock
	private ServletConfig config;
	@Mock
	private ServletContext context;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private RequestDispatcher dispatcher;
	@Mock
	private HttpSession session;

	public IndexCommandTest() {
		MockitoAnnotations.initMocks(this);
		when(request.getSession()).thenReturn(session);
		when(config.getServletContext()).thenReturn(context);
		servlet.init(config);
	}

	@Test
	public void indexForGuest() throws ServletException, IOException {
		indexLookup(null, "/WEB-INF/index.jsp");
	}

	@Test
	public void indexForUser() throws ServletException, IOException {
		User user = new User();
		user.setRole(RoleType.ROLE_USER);
		indexLookup(user, "redirect:lobby");
	}

	@Test
	public void indexForManager() throws ServletException, IOException {
		User user = new User();
		user.setRole(RoleType.ROLE_MANAGER);
		indexLookup(user, "redirect:lobby");
	}

	@Test
	public void indexForMaster() throws ServletException, IOException {
		User user = new User();
		user.setRole(RoleType.ROLE_MASTER);
		indexLookup(user, "redirect:lobby");
	}

	@Test
	public void indexForAdmin() throws ServletException, IOException {
		User user = new User();
		user.setRole(RoleType.ROLE_ADMIN);
		indexLookup(user, "redirect:users");
	}

	private void indexLookup(User user, String desiredIndex) throws ServletException, IOException {
		when(request.getRequestURI()).thenReturn("index");
		when(request.getQueryString()).thenReturn("");
		when(request.getSession().getAttribute("user")).thenReturn(user);
		when(request.getRequestDispatcher(any())).thenReturn(dispatcher);

		servlet.service(request, response);

		verify(request, times(1)).getRequestDispatcher(desiredIndex);
		verify(dispatcher).forward(request, response);
	}
}
