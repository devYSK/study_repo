package hello.itemservice.order;

/**
 * @author : ysk
 */
public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}