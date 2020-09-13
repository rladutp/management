package TaxiCall;

import TaxiCall.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    ManagementRepository managementRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_RequestConfirmOrder(@Payload Ordered ordered){

        if(ordered.isMe()){

            System.out.println("##### listener RequestConfirmOrder : " + ordered.toJson());
            Management management = new Management();

            management.setStatus("Ordered");
            management.setDriverId(ordered.getDriverId());
            management.setOrderId(ordered.getOrderId());
            management.setLocation(ordered.getLocation());

            managementRepository.save(management);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderAgreed_StatusChange(@Payload OrderAgreed orderAgreed){

        if(orderAgreed.isMe()){
            System.out.println("##### listener StatusChange_orderAgreed : " + orderAgreed.toJson());

            managementRepository.findById(Long.valueOf(orderAgreed.getOrderId())).ifPresent((Management)->{
                Management.setDriverId(orderAgreed.getDriverId());
                Management.setStatus("Approved");
                managementRepository.save(Management);
            });

            System.out.println("orderAgreed end1");
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderDeclined_StatusChange(@Payload OrderDeclined orderDeclined){

        if(orderDeclined.isMe()){
            System.out.println("##### listener StatusChange_orderDeclined : " + orderDeclined.toJson());

            managementRepository.findById(Long.valueOf(orderDeclined.getOrderId())).ifPresent((Management)->{
                Management.setDriverId(orderDeclined.getDriverId());
                Management.setStatus("Denied");
                managementRepository.save(Management);
            });

            System.out.println("orderDeclined end1");
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCanceled_RequestCancelOrder(@Payload OrderCanceled orderCanceled){
        /* 20200910 추가 확인 필요 */
        if(orderCanceled.isMe()){
            System.out.println("##### listener RequestCancelOrder : " + orderCanceled.toJson());
            Management management = new Management();

            management.setStatus("OrderCanceled");
            management.setDriverId(orderCanceled.getDriverId());
            management.setOrderId(orderCanceled.getOrderId());
            management.setLocation(orderCanceled.getLocation());

            managementRepository.save(management);
        }
    }

}
