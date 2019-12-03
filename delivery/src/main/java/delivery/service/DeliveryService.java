package delivery.service;

import core.events.delivery.AgentAssignedEvent;
import core.events.delivery.OrderDeliveredEvent;
import core.events.delivery.OrderPickedUpEvent;
import core.models.PickupOrder;
import delivery.controller.DeliveryController;
import delivery.entities.Delivery;
import delivery.entities.DeliveryAgent;
import delivery.entities.DeliveryStatus;
import delivery.entities.PendingDelivery;
import delivery.repository.PendingDeliveryRepository;
import delivery.util.DeliveryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import delivery.repository.DeliveryAgentRepository;
import delivery.repository.DeliveryRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private PendingDeliveryRepository pendingDeliveryRepository;

    @Value(value = "${kafka.event.topic}")
    private String eventTopic;

    @Autowired
    private KafkaTemplate<Long, Object> eventKafkaTemplate;

    @Transactional
    public synchronized DeliveryAgent getAndAssignAgent(){
        List<DeliveryAgent> agents = deliveryAgentRepository.findByAvailableTrue(PageRequest.of(0, 1));
        if(!agents.isEmpty()){
            DeliveryAgent agent = agents.get(0);
            long agentId = agent.getId();
            agent.setAvailable(false);
            return deliveryAgentRepository.saveAndFlush(agent);
        }
        return null;
    }

    @Transactional
    public void updateAgentAsAvailable(long agentId){
        Optional<DeliveryAgent> agentOptional = deliveryAgentRepository.findById(agentId);
        if(agentOptional.isPresent()){
            DeliveryAgent agent = agentOptional.get();
            agent.setAvailable(true);
            deliveryAgentRepository.saveAndFlush(agent);
        }
    }

    public Delivery createDelivery(PickupOrder pickupOrder){
        Delivery delivery = new Delivery();
        delivery.setDeliveryAddress(DeliveryUtil.convertAddressModelToAddressEntity(pickupOrder.getDeliveryAddress()));
        delivery.setOrderId(pickupOrder.getOrderId());
        delivery.setCustomerId(pickupOrder.getCustomerId());
        delivery.setDeliveryStatus(DeliveryStatus.AGENT_NOT_ASSIGNED);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public void assignAgent(Delivery delivery){
        DeliveryAgent agent = getAndAssignAgent();
        if(agent == null){
            PendingDelivery pendingDelivery = new PendingDelivery();
            pendingDelivery.setCreatedAt(LocalDateTime.now());
            pendingDelivery.setOrderId(delivery.getOrderId());
            pendingDelivery.setCustomerId(delivery.getCustomerId());
            pendingDelivery.setDeliveryAddress(delivery.getDeliveryAddress());
            pendingDelivery.setRestaurantId(delivery.getRestaurantId());
            pendingDeliveryRepository.saveAndFlush(pendingDelivery);
            logger.info("No agent is available, inserting into PendingDelivery table for order id "+pendingDelivery.getOrderId());
        }
        else{
            delivery.setDeliveryAgent(agent);
            delivery.setDeliveryStatus(DeliveryStatus.AGENT_ASSIGNED);
            deliveryRepository.saveAndFlush(delivery);
            AgentAssignedEvent agentAssignedEvent = new AgentAssignedEvent();
            agentAssignedEvent.setAgentId(delivery.getDeliveryAgent().getId());
            agentAssignedEvent.setCustomerId(delivery.getCustomerId());
            agentAssignedEvent.setOrderId(delivery.getOrderId());
            agentAssignedEvent.setRestaurantId(delivery.getRestaurantId());
            agentAssignedEvent.setDeliveryAddress(DeliveryUtil.convertAddressEntityToAddressModel(delivery.getDeliveryAddress()));
            eventKafkaTemplate.send(eventTopic,agentAssignedEvent.getOrderId(),agentAssignedEvent);
            eventKafkaTemplate.flush();
            logger.info("Agent "+agent.getContactName()+"assigned to "+delivery.getOrderId());
        }
    }

    public void setDeliveryStateToPickedUp(long orderId){
       Optional<Delivery> optionalDelivery = deliveryRepository.findByOrderId(orderId);
       if(optionalDelivery.isPresent()){
           Delivery delivery =optionalDelivery.get();
           delivery.setPickedUpAt(LocalDateTime.now());
           delivery.setDeliveryStatus(DeliveryStatus.PICKED_UP);
           deliveryRepository.saveAndFlush(delivery);
           OrderPickedUpEvent orderPickedUpEvent = new OrderPickedUpEvent();
           orderPickedUpEvent.setCustomerId(delivery.getCustomerId());
           orderPickedUpEvent.setDeliveryAddress(DeliveryUtil.convertAddressEntityToAddressModel(delivery.getDeliveryAddress()));
           orderPickedUpEvent.setAgentId(delivery.getDeliveryAgent().getId());
           orderPickedUpEvent.setOrderId(delivery.getOrderId());
           eventKafkaTemplate.send(eventTopic,orderPickedUpEvent.getOrderId(),orderPickedUpEvent);
           eventKafkaTemplate.flush();
       }
    }

    @Transactional
    public void setDeliveryStateToDelivered(long orderId){
        Optional<Delivery> optionalDelivery = deliveryRepository.findByOrderId(orderId);
        if(optionalDelivery.isPresent()){
            Delivery delivery =optionalDelivery.get();
            delivery.setDeliveredAt(LocalDateTime.now());
            delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
            deliveryRepository.saveAndFlush(delivery);
            OrderDeliveredEvent orderDeliveredEvent = new OrderDeliveredEvent();
            orderDeliveredEvent.setCustomerId(delivery.getCustomerId());
            orderDeliveredEvent.setAgentId(delivery.getDeliveryAgent().getId());
            orderDeliveredEvent.setOrderId(delivery.getOrderId());
            eventKafkaTemplate.send(eventTopic,orderDeliveredEvent.getOrderId(),orderDeliveredEvent);
            eventKafkaTemplate.flush();
            Optional<DeliveryAgent> deliveryAgentOptional = deliveryAgentRepository.findById(delivery.getDeliveryAgent().getId());
            if(deliveryAgentOptional.isPresent()){
                DeliveryAgent deliveryAgent =deliveryAgentOptional.get();
                deliveryAgent.setAvailable(true);
                deliveryAgentRepository.saveAndFlush(deliveryAgent);
                logger.info("DeliveryAgent is persisted,availability set to true for agent id:"+deliveryAgent.getId());
            }
        }
    }
}
