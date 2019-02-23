package aklny;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;


/**
 * Servlet implementation class aklny
 */
@WebServlet("/")
public class aklny extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DataSource ds;
    private EntityManagerFactory emf;
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");

            Map properties = new HashMap();
            properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
            emf = Persistence.createEntityManagerFactory("persistence-with-jpa", properties);
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        emf.close();
    }

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public aklny() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.getWriter().println("<p>Aknly!</p>");
		
		response.getWriter().println(getLoginForm());
	}
	
	private String getLoginForm() {
		String respond = "<select name=\"username\" form=\"userform\">\n";
		List<String> odataUsernames = new OdataReader().getDefaultEmployeesUsernames();
		for (String username : odataUsernames) {
			respond += "<option value=\"" + username + "\">" + username + "</option>\n";
		}
		respond += "</select>\n" + 
				"\n" + 
				"<form action=\"\" method=\"post\" id=\"userform\">\n" + 
				"  password:<input type=\"password\" name=\"password\">\n" + 
				"  <input type=\"submit\">\n" + 
				"</form>\n";
		return respond;
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		String username = request.getParameter("username");
        String password = request.getParameter("password");
		if (username != null && !username.isEmpty())
		{
			EntityManager em = emf.createEntityManager();
			@SuppressWarnings("unchecked")
			List<Employee> employees = em.createNamedQuery("employee")
					.setParameter("username", username)
					.getResultList();
			Employee employee = null;
			if (employees.isEmpty())
			{
				// Add new employee
				employee = new Employee();
				employee.setUsername(username);
				// use 1234 as default pass
				employee.setPassword("1234");
				em.getTransaction().begin();
	            em.persist(employee);
	            em.getTransaction().commit();
			}
			else 
			{
				// get first employee
				// there is only one btw 
				employee = employees.get(0);
			
				// check on date
				
			}
			// check on password and return error if wrong password 
			//  may be later :) 
			if (!employee.getPassword().equals(password))
			{
				response.getWriter().println("wrong Password");
				return;
			}
			if(canSubmit())
			{
				// view the dishes submission page
				response.getWriter().println(getDishesForm(em, employee));
			}
			else 
			{
				// or display the chosen dish 
				response.sendRedirect("dish_registration?employee_id=" + employee.getId());
			}
		}
		
			
	}
	
	private boolean canSubmit() {
			LocalTime now = LocalTime.now();
			// these variables changes according to server time :)
			return now.isBefore(LocalTime.of(10, 0)) || now.isAfter(LocalTime.of(13, 0));
	}
	
	private String getDishesForm(EntityManager em, Employee employee)
	{
		@SuppressWarnings("unchecked")
        List<Dish> resultList = em.createNamedQuery("dishes").getResultList();
		@SuppressWarnings("unchecked")
		List<Location> locationList = em.createNamedQuery("locations").getResultList();
		String respond = "\n" + 
				"<h1>Available Dishes</h1>\n" + 
				"<form  method=\"post\" action=\"dish_registration\">";
		for (Dish dish : resultList) {
			respond += "<input type=\"radio\" name=\"dish_id\" value=\"" + dish.getId() + "\">" + dish.getDescription() + "<br>\n";
		}
		respond += "<h1>Available locations</h1>\n";
		for (Location location : locationList) {
			respond += "<input type=\"radio\" name=\"location_id\" value=\"" +
					location.getId() + "\">" +
					location.getName() + "<br>\n";
		}
		respond +=
				"<input type=\"hidden\" name=\"employee_id\" value=\""+ employee.getId() + "\">\n" +
				"<input type=\"submit\" value=\"Submit\">\n" +
			    "</form>\n";
		return respond;
	}

}
