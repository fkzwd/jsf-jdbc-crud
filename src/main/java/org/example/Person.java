package org.example;

import javax.faces.bean.ManagedBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext; 
import java.util.Map; 

@ManagedBean
public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private int weight;

	public Person() {
		
	}
	
	public Person(int id, String firstName, String lastName, int age, int weight) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.weight = weight;
	}
	
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<Person> getAllPersons(){
        List<Person> result = new ArrayList<Person>();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM persons ORDER BY id ASC");
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                Person person = new Person();
                person.setId(rs.getInt("id"));
                person.setFirstName(rs.getString("firstName"));
                person.setLastName(rs.getString("lastName"));
                person.setAge(rs.getInt("age"));
                person.setWeight(rs.getInt("weight"));
                result.add(person);
            }
        }
        catch (Exception e) {
			Person p = new Person();
			p.setId(0);
			p.setFirstName("Not loaded from db");
			p.setLastName("Not loaded from db");
			p.setAge(0);
			p.setWeight(0);
			result.add(p);
        }
        return result;
    }
	
	public String save(){
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement p = conn.prepareStatement("INSERT INTO persons (\"firstName\",\"lastName\",age,weight) VALUES(?,?,?,?);");
			p.setString(1,getFirstName());
			p.setString(2,getLastName());
			p.setInt(3,getAge());
			p.setInt(4,getWeight());
			p.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
			return "/create.xhtml?faces-redirect=true";
		}
		return "/index.xhtml?faces-redirect=true";
	}
	
	public String updateThis(){
		Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			Statement statement = connection.createStatement();
			statement.execute("SELECT * FROM persons WHERE id="+getId());
			ResultSet rs = statement.getResultSet();
			if (!rs.next()) {
				return "/index.xhtml";
			}
			Person p = new Person(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
									rs.getInt("age"), rs.getInt("weight"));
			sessionMap.put("person",p);
			return "/update.xhtml?faces-redirect=true";
		}
		catch (SQLException e) {
			e.printStackTrace();
			return "/index.xhtml?faces-redirect=true";
		}
	}
	
	public String deleteThis(){
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			Statement statement = connection.createStatement();
			statement.execute("DELETE FROM persons WHERE id="+getId());
			return "/index.xhtml?faces-redirect=true";
		}
		catch (SQLException e) {
			e.printStackTrace();
			return "/index.xhtml?faces-redirect=true";
		}
	}
	

	public String update(){
        try {
			PreparedStatement p = DBConnection.getInstance().getConnection().prepareStatement("UPDATE PERSONS SET \"firstName\"=?,"+
									"\"lastName\"=?, age=?, weight=? WHERE ID = ?");
			p.setString(1,getFirstName());
			p.setString(2,getLastName());
			p.setInt(3,getAge());
			p.setInt(4,getWeight());
			p.setInt(5,getId());
			p.execute();
			Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			sessionMap.remove("person");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "/update.xhtml";
        }
        return "/index.xhtml?faces-redirect=true";
    }
}
