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


import aklny.Location;

/**
 * Servlet implementation class DishSeeder
 */
@WebServlet("/LocationSeeder")
public class LocationSeeder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private DataSource ds;
    private EntityManagerFactory emf;
	
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public LocationSeeder() throws ServletException {
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
		runLocationSeeder(emf.createEntityManager());
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	public static void runLocationSeeder(EntityManager em) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 2; i++) {
			Location location = new Location();
			location.setName("Location" + i);
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
