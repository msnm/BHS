import be.kdg.bhs.organizer.api.MessageConsumerListener;
import be.kdg.bhs.organizer.api.MessageConsumerService;
import be.kdg.bhs.organizer.api.MessageFormatterService;
import be.kdg.bhs.organizer.jms.ConsumerLogic;
import java.util.List;

/**
 * @author Michael
 * @project BHS
 * A mock implementation of a messageConsumerService for testing purposes.
 * This makes it possible to mock the messagebrokers like Rabbitmq.
 */
public class MockConsumer implements MessageConsumerService {

    private List<String> messages;
    private long intervalToConsumeMessage;
    private long timeToSleepBeforeStart;

    /**
     *
     * @param intervalToConsumeMessage interval on which the messages are consumed
     * @param timeToSleepBeforeStart is needed, to let for example the suitcase message starting consuming earlier then the sensormessages
     */
    public MockConsumer(long intervalToConsumeMessage, long timeToSleepBeforeStart) {
        this.intervalToConsumeMessage = intervalToConsumeMessage;
        this.timeToSleepBeforeStart = timeToSleepBeforeStart;
    }

    @Override
    public void initialize(MessageConsumerListener messageConsumerListener, MessageFormatterService messageFormatter, Class aClass) {
        ConsumerLogic consumerLogic = new ConsumerLogic();
        String message=null;

        try {
            Thread.sleep(timeToSleepBeforeStart);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(() -> {
            while (messages.size()!=0) {
                try {
                    Thread.sleep(intervalToConsumeMessage);

                            consumerLogic.consumeMessage(messageConsumerListener, messageFormatter, messages.get(0), aClass);
                            messages.remove(0);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
       // thread.setDaemon(true);
        thread.start();

    }

    @Override
    public void shutdown() {

    }

    public List<String> getSuitcasemessages() {
        return messages;
    }

    public void setSuitcasemessages(List<String> suitcasemessages) {
        this.messages = suitcasemessages;
    }
}
