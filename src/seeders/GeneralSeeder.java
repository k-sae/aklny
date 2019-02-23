package seeders;

import java.io.IOException;
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
 * Servlet implementation class DishSeeder
 */

@WebServlet("/general_seeder")
public class GeneralSeeder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private DataSource ds;
    private EntityManagerFactory emf;
	
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public GeneralSeeder() throws ServletException {
        super();
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
		
		EntityManager entityManager = emf.createEntityManager(); 
		DishSeeder.runDishSeeder(entityManager);
		LocationSeeder.runLocationSeeder(entityManager);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	// i should have created a parent class where any seeder can inherit from :/
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
