package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.dto.DTOtoEO;
import be.kdg.bhs.organizer.dto.MessageDTO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;
import be.kdg.bhs.organizer.model.Suitcase;
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
    private FlightService flightService;

    public RoutingServiceImpl(MessageConsumerService messageConsumerService, MessageFormatterService messageFormatterService, FlightService flightService) {
        this.messageConsumerService = messageConsumerService;
        this.messageFormatterService = messageFormatterService;
        this.flightService = flightService;
    }


    public void start() {
        this.messageConsumerService.initialize(this,messageFormatterService);

    }

    @Override
    public void onReceive(SuitcaseMessageDTO messageDTO) {
        logger.info("Entered onReceive(): Suitcase {} is being processed",messageDTO.getId());
        Suitcase suitcase = DTOtoEO.suitCaseDTOtoEO(messageDTO);

        if(flightService!=null) {
            suitcase.setBoardingConveyorId(flightService.flightInFormation(suitcase.getFlightNumber()));
        }




        System.out.println("Handling the message");
        System.out.println(messageDTO.toString());
    }

    @Override
    public void onError() {

    }
}
