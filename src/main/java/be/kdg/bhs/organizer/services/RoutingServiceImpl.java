package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.MessageConsumerListener;
import be.kdg.bhs.organizer.api.MessageConsumerService;
import be.kdg.bhs.organizer.api.MessageFormatterService;
import be.kdg.bhs.organizer.api.MessageProducerService;
import be.kdg.bhs.organizer.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.MessageHeader;

/**
 * @author Michael
 * @project BHS
 */
public class RoutingServiceImpl implements MessageConsumerListener {

    private Logger logger = LoggerFactory.getLogger(RoutingServiceImpl.class);
    private MessageConsumerService messageConsumerService;
    private MessageFormatterService messageFormatterService;

    public RoutingServiceImpl(MessageConsumerService messageConsumerService, MessageFormatterService messageFormatterService) {
        this.messageConsumerService = messageConsumerService;
        this.messageFormatterService = messageFormatterService;
    }

    public void start() {
        this.messageConsumerService.initialize(this,messageFormatterService);

    }

    @Override
    public void onReceive(MessageDTO messageDTO) {

    }

    @Override
    public void onError() {

    }
}
