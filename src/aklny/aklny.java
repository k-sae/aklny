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
import org.json.JSONObject;

import com.google.gson.Gson;


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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().print(new OdataReader().getEmployeesJson());
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		String username = request.getParameter("username");
        String password = request.getParameter("password");
        response.addHeader("Access-Control-Allow-Origin", "*");
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
			if (!employee.getPassword().equals(password))
			{
				response.sendError(402);
				response.getWriter().println("wrong Password");
				return;
			}
			if(Utils.canSubmit())
			{
				// view the dishes submission page
				JSONObject obj = new JSONObject();
				obj.put("user_id", employee.getId());
				obj.put("status", "can_register");
				response.getWriter().println(obj.toString());
			}
			else 
			{
				// view the dishes submission page
				JSONObject obj = new JSONObject();
				obj.put("user_id", employee.getId());
				obj.put("status", "cant_register");
				response.getWriter().println(obj.toString());
			}
		}
		
			
	}
	
	// for testing, no need for this atm 
	// TODO remove later 
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
