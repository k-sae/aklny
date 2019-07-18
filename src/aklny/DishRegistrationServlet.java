package aklny;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
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

import com.google.gson.Gson;

/**
 * Servlet implementation class DishRegistrationServlet
 */
@WebServlet("/dish_registration")
public class DishRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    private DataSource ds;
    private EntityManagerFactory emf;
	

    public DishRegistrationServlet() throws ServletException {
        super();
        // TODO Auto-generated constructor stub
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");

            Map<String, DataSource> properties = new HashMap<String, DataSource>();
            properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
            emf = Persistence.createEntityManagerFactory("persistence-with-jpa", properties);
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		 EntityManager em = emf.createEntityManager();
	    
	     DishResponse dishResponse =  new DishResponse();
	     dishResponse.dishes = em.createNamedQuery("dishes").getResultList();
	     dishResponse.locations = em.createNamedQuery("locations").getResultList();
         // not the best practice though
	     response.addHeader("Access-Control-Allow-Origin", "*");
	     response.getWriter().print(new Gson().toJson(dishResponse));

//	     
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String employeeId = request.getParameter("employee_id");
        String dishId = request.getParameter("dish_id");
        String locationId = request.getParameter("location_id");
        // TODO add validation on these params 
        // may be later
        response.addHeader("Access-Control-Allow-Origin", "*");
        if(Utils.isNotValid(employeeId) || Utils.isNotValid(dishId)|| Utils.isNotValid(locationId))
        {
        	response.setStatus(422);
        	response.getWriter().println(new Response("failed", "please choose dish and location"));
        	return;
        }
        
        if(!Utils.canSubmit())
        {
        	response.setStatus(422);
        	response.getWriter().println(new Response("failed", "Invalid Time, cant change the chosen dish"));
        	return;
  
        }
        EntityManager em = emf.createEntityManager();
        Employee employee =  em.find(Employee.class, Long.valueOf(employeeId));
        em.getTransaction().begin();
        employee.setDishId(Integer.valueOf(dishId));
        // the date of choice in case logging in in another day
        employee.setChoiceDate(Date.valueOf(LocalDate.now()));
        employee.setChoiceLocationId(Integer.valueOf(locationId));
        em.getTransaction().commit();
        
        response.getWriter().println(new Response("sucess", "Ur Choice was recorded"));
        
	}
	
	
	@SuppressWarnings("unused")
	private class DishResponse
	{
		public List<Dish> dishes;
		public List<Location> locations;
	}
	
	
}
