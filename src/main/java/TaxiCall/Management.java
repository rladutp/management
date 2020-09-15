package TaxiCall;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Management_table")
public class Management {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long orderId;
    private String status;
    private Long driverId;
    private String location;

    @PostPersist
    public void onPostPersist(){
        System.out.println("## seq 0 :  "+this.getStatus());
        /*
        CheckOrderRequested checkOrderRequested = new CheckOrderRequested();
        BeanUtils.copyProperties(this, checkOrderRequested);
        checkOrderRequested.publishAfterCommit();
        */

        System.out.println("## seq 1 ");
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        if(this.getStatus().equals("Ordered")) {
            TaxiCall.external.Driver driver = new TaxiCall.external.Driver();
            // mappings goes here

            driver.setStatus("Ordered");
            driver.setDriverId(this.getDriverId());
            driver.setOrderId(this.getOrderId());
            driver.setLocation(this.getLocation());

            System.out.println(this.getDriverId() + "TEST 1 : " + this.getOrderId());

            ManagementApplication.applicationContext.getBean(TaxiCall.external.DriverService.class)
                    .checkOrder(driver);

            System.out.println("AAAA");
        }

        /* 20200910 추가 확인 필요 */
        else if(this.getStatus().equals("OrderCanceled")) {
            CancelOrderRequested cancelOrderRequested = new CancelOrderRequested();

            cancelOrderRequested.setOrderId(this.getOrderId());
            cancelOrderRequested.setStatus(this.getStatus());
            cancelOrderRequested.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, cancelOrderRequested);
            cancelOrderRequested.publishAfterCommit();
        }
    }

    @PostUpdate
    public void onPostUpdate(){
        System.out.println("START1");
        System.out.println("Status  :  "+this.getStatus());
        if(this.getStatus().equals("Approved")) {
            System.out.println("test approved");
            OrderApproved orderApproved = new OrderApproved();

            orderApproved.setOrderId(this.getOrderId());
            orderApproved.setStatus(this.getStatus());
            orderApproved.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, orderApproved);
            orderApproved.publishAfterCommit();
        }

        else if(this.getStatus().equals("Denied")) {
            System.out.println("test denied");
            OrderDenied orderDenied = new OrderDenied();

            orderDenied.setOrderId(this.getOrderId());
            orderDenied.setStatus(this.getStatus());
            orderDenied.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, orderDenied);
            orderDenied.publishAfterCommit();

        } else if(this.getStatus().equals("OrderCanceled")) {
            System.out.println("test OrderCanceled");
            CancelOrderRequested cancelOrderRequested = new CancelOrderRequested();

            cancelOrderRequested.setOrderId(this.getOrderId());
            cancelOrderRequested.setStatus(this.getStatus());
            cancelOrderRequested.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, cancelOrderRequested);
            cancelOrderRequested.publishAfterCommit();

        }
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
