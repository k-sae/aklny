package aklny;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
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
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DishRegistrationServlet() throws ServletException {
        super();
        // TODO Auto-generated constructor stub
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String employeeId = request.getParameter("employee_id");
		
		 EntityManager em = emf.createEntityManager();
	     Employee employee =  em.find(Employee.class, Long.valueOf(employeeId));
	    
	     
	     if	(employee.getDishId() != 0 && employee.getChoiceDate().toLocalDate().isEqual(LocalDate.now()))
	     {
	    	 Dish dish =  em.find(Dish.class, Long.valueOf(employee.getDishId()));
	    	 response.getWriter().println("Ur Dish is: " + dish.getDescription());
	     }
	     else 
	    	 response.getWriter().println("U didnt choose a dish for today ");
	     
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String employeeId = request.getParameter("employee_id");
        String dishId = request.getParameter("dish_id");
        String locationId = request.getParameter("location_id");
        // TODO add location
        // TODO add validation on these params 
        // may be later
        if(employeeId == null || dishId == null || locationId == null)
        {
        	response.getWriter().println("please choose dish and location");
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
        response.sendRedirect("?employee_id=" + employeeId);
        
        
	}

}
