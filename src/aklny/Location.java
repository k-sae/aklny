package aklny;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Location
 *
 */
@Entity
@Table(name = "T_LOCATION")
@NamedQuery(name = "locations", query = "select l from Location l")
public class Location implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    @Basic
    private String name;
    
	private static final long serialVersionUID = 1L;

	public Location() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
   
}
