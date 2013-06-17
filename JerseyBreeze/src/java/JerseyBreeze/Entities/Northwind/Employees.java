package JerseyBreeze.Entities.Northwind;
// Generated Jun 16, 2013 10:02:11 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Employees generated by hbm2java
 */
@Entity
@Table(name="Employees"
    ,schema="dbo"
    ,catalog="NorthwindIB"
)
public class Employees  implements java.io.Serializable {


     private int employeeId;
     private Employees employees;
     private String lastName;
     private String firstName;
     private String title;
     private String titleOfCourtesy;
     private Date birthDate;
     private Date hireDate;
     private String address;
     private String city;
     private String region;
     private String postalCode;
     private String country;
     private String homePhone;
     private String extension;
     private byte[] photo;
     private String notes;
     private String photoPath;
     private Set territorieses = new HashSet(0);
     private Set orderses = new HashSet(0);
     private Set employeeses = new HashSet(0);

    public Employees() {
    }

	
    public Employees(int employeeId, String lastName, String firstName) {
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
    }
    public Employees(int employeeId, Employees employees, String lastName, String firstName, String title, String titleOfCourtesy, Date birthDate, Date hireDate, String address, String city, String region, String postalCode, String country, String homePhone, String extension, byte[] photo, String notes, String photoPath, Set territorieses, Set orderses, Set employeeses) {
       this.employeeId = employeeId;
       this.employees = employees;
       this.lastName = lastName;
       this.firstName = firstName;
       this.title = title;
       this.titleOfCourtesy = titleOfCourtesy;
       this.birthDate = birthDate;
       this.hireDate = hireDate;
       this.address = address;
       this.city = city;
       this.region = region;
       this.postalCode = postalCode;
       this.country = country;
       this.homePhone = homePhone;
       this.extension = extension;
       this.photo = photo;
       this.notes = notes;
       this.photoPath = photoPath;
       this.territorieses = territorieses;
       this.orderses = orderses;
       this.employeeses = employeeses;
    }
   
     @Id 
    
    @Column(name="EmployeeID", unique=true, nullable=false)
    public int getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ReportsTo")
    public Employees getEmployees() {
        return this.employees;
    }
    
    public void setEmployees(Employees employees) {
        this.employees = employees;
    }
    
    @Column(name="LastName", nullable=false)
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    @Column(name="FirstName", nullable=false)
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Column(name="Title")
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Column(name="TitleOfCourtesy")
    public String getTitleOfCourtesy() {
        return this.titleOfCourtesy;
    }
    
    public void setTitleOfCourtesy(String titleOfCourtesy) {
        this.titleOfCourtesy = titleOfCourtesy;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="BirthDate", length=23)
    public Date getBirthDate() {
        return this.birthDate;
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="HireDate", length=23)
    public Date getHireDate() {
        return this.hireDate;
    }
    
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    
    @Column(name="Address")
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    @Column(name="City")
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    @Column(name="Region")
    public String getRegion() {
        return this.region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    @Column(name="PostalCode")
    public String getPostalCode() {
        return this.postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    @Column(name="Country")
    public String getCountry() {
        return this.country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    @Column(name="HomePhone")
    public String getHomePhone() {
        return this.homePhone;
    }
    
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }
    
    @Column(name="Extension")
    public String getExtension() {
        return this.extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    @Column(name="Photo")
    public byte[] getPhoto() {
        return this.photo;
    }
    
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    
    @Column(name="Notes")
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Column(name="PhotoPath")
    public String getPhotoPath() {
        return this.photoPath;
    }
    
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="EmployeeTerritories", schema="dbo", catalog="NorthwindIB", joinColumns = { 
        @JoinColumn(name="EmployeeID", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="TerritoryID", nullable=false, updatable=false) })
    public Set getTerritorieses() {
        return this.territorieses;
    }
    
    public void setTerritorieses(Set territorieses) {
        this.territorieses = territorieses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="employees")
    public Set getOrderses() {
        return this.orderses;
    }
    
    public void setOrderses(Set orderses) {
        this.orderses = orderses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="employees")
    public Set getEmployeeses() {
        return this.employeeses;
    }
    
    public void setEmployeeses(Set employeeses) {
        this.employeeses = employeeses;
    }




}


