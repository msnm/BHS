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
    private ConveyorService conveyorService;

    public RoutingServiceImpl(MessageConsumerService messageConsumerService, MessageFormatterService messageFormatterService, FlightService flightService, ConveyorService conveyorService) {
        this.messageConsumerService = messageConsumerService;
        this.messageFormatterService = messageFormatterService;
        this.flightService = flightService;
        this.conveyorService = conveyorService;
    }

    public void start() {
        this.messageConsumerService.initialize(this,messageFormatterService);

    }

    @Override
    public void onReceive(SuitcaseMessageDTO messageDTO) {
        logger.info("Entered onReceive(): Suitcase {} is being processed",messageDTO.getId());
        Suitcase suitcase = DTOtoEO.suitCaseDTOtoEO(messageDTO);

        System.out.println("Handling the message");
        System.out.println(messageDTO.toString());

        if(flightService!=null) {
            suitcase.setBoardingConveyorId(flightService.flightInFormation(suitcase.getFlightNumber()));
        }
        else {
            //TODO EXCEPTION
        }

        if(conveyorService!=null) {
            conveyorService.routeInformation(suitcase).getRoutes().forEach(v -> System.out.println(v));
        }
        else {
            //TODO EXCEPTION
        }




    }

    @Override
    public void onError() {

    }
}
