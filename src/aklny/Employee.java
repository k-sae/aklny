package aklny;


import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "T_EMPLOYEE")
@NamedQuery(name = "employee", query = "from Employee e where e.username=:username")
public class Employee {
    @Id
    @GeneratedValue
    private long id;
    @Basic
    private String username;
    @Basic
    private String password;
    @Basic(optional = true)
    private int dishId;
    @Basic(optional = true)
    private Date choiceDate;
    @Basic(optional = true)
    private int choiceLocationId;
    
    
    public long getId() {
        return id;
    }

    public void setId(long newId) {
        this.id = newId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public int getDishId() {
		return dishId;
	}

	public void setDishId(int dishId) {
		this.dishId = dishId;
	}

	public Date getChoiceDate() {
		return choiceDate;
	}

	public void setChoiceDate(Date choiceDate) {
		this.choiceDate = choiceDate;
	}

	public int getChoiceLocationId() {
		return choiceLocationId;
	}

	public void setChoiceLocationId(int choiceLocationId) {
		this.choiceLocationId = choiceLocationId;
	}
}