package core.states;
/*
* ORDER_CREATED
* payment service is request to verify balance
*
*   If unsuccessful
*       publish PaymentFailedEvent then ORDER_REJECTED
*
*   else
*       publish PaymentSucceededEvent then
*       ->ORDER_ACCEPTED
*       ->OrderPreparationCompletedEvent
*       ->OrderPickedUpEvent
*       ->OrderDeliveredEvent
*
* */
public enum OrderState {
    ORDER_CREATED,
    ORDER_ACCEPTED,
    ORDER_REJECTED,
    ORDER_PREPARATION_COMPLETED,
    ORDER_PICKED_UP,
    ORDER_DELIVERED
}
