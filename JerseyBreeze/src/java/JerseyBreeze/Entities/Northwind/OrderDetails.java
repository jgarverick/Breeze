package JerseyBreeze.Entities.Northwind;
// Generated Jun 16, 2013 10:02:11 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * OrderDetails generated by hbm2java
 */
@Entity
@Table(name="Order Details"
    ,schema="dbo"
    ,catalog="NorthwindIB"
)
public class OrderDetails  implements java.io.Serializable {


     private OrderDetailsId id;
     private BigDecimal unitPrice;
     private short quantity;
     private float discount;

    public OrderDetails() {
    }

    public OrderDetails(OrderDetailsId id, BigDecimal unitPrice, short quantity, float discount) {
       this.id = id;
       this.unitPrice = unitPrice;
       this.quantity = quantity;
       this.discount = discount;
    }
   
     @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="orderId", column=@Column(name="OrderID", nullable=false) ), 
        @AttributeOverride(name="productId", column=@Column(name="ProductID", nullable=false) ) } )
    public OrderDetailsId getId() {
        return this.id;
    }
    
    public void setId(OrderDetailsId id) {
        this.id = id;
    }
    
    @Column(name="UnitPrice", nullable=false, scale=4)
    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    @Column(name="Quantity", nullable=false)
    public short getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(short quantity) {
        this.quantity = quantity;
    }
    
    @Column(name="Discount", nullable=false, precision=24, scale=0)
    public float getDiscount() {
        return this.discount;
    }
    
    public void setDiscount(float discount) {
        this.discount = discount;
    }




}

