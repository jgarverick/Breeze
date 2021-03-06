package JerseyBreeze.Entities.Northwind;
// Generated Jun 16, 2013 10:02:11 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Orders generated by hbm2java
 */
@Entity
@Table(name="Orders"
    ,schema="dbo"
    ,catalog="NorthwindIB"
)
public class Orders  implements java.io.Serializable {


     private int orderId;
     private Employees employees;
     private Customers customers;
     private Shippers shippers;
     private Date orderDate;
     private Date requiredDate;
     private Date shippedDate;
     private BigDecimal freight;
     private String shipName;
     private String shipAddress;
     private String shipCity;
     private String shipRegion;
     private String shipPostalCode;
     private String shipCountry;

    public Orders() {
    }

	
    public Orders(int orderId) {
        this.orderId = orderId;
    }
    public Orders(int orderId, Employees employees, Customers customers, Shippers shippers, Date orderDate, Date requiredDate, Date shippedDate, BigDecimal freight, String shipName, String shipAddress, String shipCity, String shipRegion, String shipPostalCode, String shipCountry) {
       this.orderId = orderId;
       this.employees = employees;
       this.customers = customers;
       this.shippers = shippers;
       this.orderDate = orderDate;
       this.requiredDate = requiredDate;
       this.shippedDate = shippedDate;
       this.freight = freight;
       this.shipName = shipName;
       this.shipAddress = shipAddress;
       this.shipCity = shipCity;
       this.shipRegion = shipRegion;
       this.shipPostalCode = shipPostalCode;
       this.shipCountry = shipCountry;
    }
   
     @Id 
    
    @Column(name="OrderID", unique=true, nullable=false)
    public int getOrderId() {
        return this.orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="EmployeeID")
    public Employees getEmployees() {
        return this.employees;
    }
    
    public void setEmployees(Employees employees) {
        this.employees = employees;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CustomerID")
    public Customers getCustomers() {
        return this.customers;
    }
    
    public void setCustomers(Customers customers) {
        this.customers = customers;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ShipVia")
    public Shippers getShippers() {
        return this.shippers;
    }
    
    public void setShippers(Shippers shippers) {
        this.shippers = shippers;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="OrderDate", length=23)
    public Date getOrderDate() {
        return this.orderDate;
    }
    
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="RequiredDate", length=23)
    public Date getRequiredDate() {
        return this.requiredDate;
    }
    
    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ShippedDate", length=23)
    public Date getShippedDate() {
        return this.shippedDate;
    }
    
    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }
    
    @Column(name="Freight", scale=4)
    public BigDecimal getFreight() {
        return this.freight;
    }
    
    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }
    
    @Column(name="ShipName")
    public String getShipName() {
        return this.shipName;
    }
    
    public void setShipName(String shipName) {
        this.shipName = shipName;
    }
    
    @Column(name="ShipAddress")
    public String getShipAddress() {
        return this.shipAddress;
    }
    
    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }
    
    @Column(name="ShipCity")
    public String getShipCity() {
        return this.shipCity;
    }
    
    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }
    
    @Column(name="ShipRegion")
    public String getShipRegion() {
        return this.shipRegion;
    }
    
    public void setShipRegion(String shipRegion) {
        this.shipRegion = shipRegion;
    }
    
    @Column(name="ShipPostalCode")
    public String getShipPostalCode() {
        return this.shipPostalCode;
    }
    
    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }
    
    @Column(name="ShipCountry")
    public String getShipCountry() {
        return this.shipCountry;
    }
    
    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }




}


