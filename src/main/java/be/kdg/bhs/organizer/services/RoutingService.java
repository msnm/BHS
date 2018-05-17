package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.dto.DTOtoEO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;
import be.kdg.bhs.organizer.model.Suitcase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michael
 * @project BHS
 * Controller of the routing proces for incoming suitcases.
 */
public class RoutingService implements MessageConsumerListener {


    private Logger logger = LoggerFactory.getLogger(RoutingService.class);
    private MessageConsumerService messageConsumerService;
    private MessageProducerService producerService;
    private MessageFormatterService messageFormatterService;
    private FlightService flightService;
    private ConveyorService conveyorService;
    private CalculateRouteService calculateRouteService;

    public RoutingService(MessageConsumerService messageConsumerService, MessageProducerService producerService, MessageFormatterService messageFormatterService, FlightService flightService, ConveyorService conveyorService, CalculateRouteService calculateRouteService) {
        this.messageConsumerService = messageConsumerService;
        this.producerService = producerService;
        this.messageFormatterService = messageFormatterService;
        this.flightService = flightService;
        this.conveyorService = conveyorService;
        this.calculateRouteService = calculateRouteService;
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
            System.out.println("BoardingConveyor:" +suitcase.getBoardingConveyorId());
        }
        else {
            //TODO EXCEPTION
        }

        if(conveyorService!=null) {
            conveyorService.routeInformation(suitcase).getRoutes().forEach(v -> System.out.println(v));
            System.out.println();
        }
        else {
            //TODO EXCEPTION
        }




    }

    @Override
    public void onError() {

    }
}
