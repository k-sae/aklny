package aklny;

import java.io.IOException;
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
 * Servlet implementation class ResultServlet
 */
@WebServlet("/Result")
public class ResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResultServlet() {
        super();
    }
    
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		EntityManager em = emf.createEntityManager();
		String employeeId = request.getParameter("employee_id");
		// not the best practice though
	    response.addHeader("Access-Control-Allow-Origin", "*");
		if(Utils.isNotValid(employeeId))
		{
			response.setStatus(422);
        	response.getWriter().println(new Response("failed", "Employee Id not found"));
        	return;
		}
		
		Employee employee =  em.find(Employee.class, Long.valueOf(employeeId));
		
		if(employee == null)
		{
			response.setStatus(422);
        	response.getWriter().println(new Response("failed", "Employee Not found"));
        	return;
		}
		
	     if	(employee.getDishId() != 0 && employee.getChoiceDate().toLocalDate().isEqual(LocalDate.now()))
	     {
	    	 Dish dish =  em.find(Dish.class, Long.valueOf(employee.getDishId()));
	    	 response.getWriter().println(new Gson().toJson(dish));
	     }
	     else 
	    	 response.getWriter().println(new Gson().toJson(new Dish()));
	}

}
