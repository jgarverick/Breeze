package JerseyBreeze.Entities.Northwind;
// Generated Jun 16, 2013 10:02:11 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Shippers generated by hbm2java
 */
@Entity
@Table(name="Shippers"
    ,schema="dbo"
    ,catalog="NorthwindIB"
)
public class Shippers  implements java.io.Serializable {


     private int shipperId;
     private String companyName;
     private String phone;
     private Set orderses = new HashSet(0);

    public Shippers() {
    }

	
    public Shippers(int shipperId, String companyName) {
        this.shipperId = shipperId;
        this.companyName = companyName;
    }
    public Shippers(int shipperId, String companyName, String phone, Set orderses) {
       this.shipperId = shipperId;
       this.companyName = companyName;
       this.phone = phone;
       this.orderses = orderses;
    }
   
     @Id 
    
    @Column(name="ShipperID", unique=true, nullable=false)
    public int getShipperId() {
        return this.shipperId;
    }
    
    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }
    
    @Column(name="CompanyName", nullable=false)
    public String getCompanyName() {
        return this.companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    @Column(name="Phone")
    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="shippers")
    public Set getOrderses() {
        return this.orderses;
    }
    
    public void setOrderses(Set orderses) {
        this.orderses = orderses;
    }




}

