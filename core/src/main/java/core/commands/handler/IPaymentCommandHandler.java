package core.commands.handler;

import core.commands.payment.IPaymentCommand;

public interface IPaymentCommandHandler {
    boolean canHandle(IPaymentCommand deliveryCommand);
    void handle(IPaymentCommand deliveryCommand);
}
