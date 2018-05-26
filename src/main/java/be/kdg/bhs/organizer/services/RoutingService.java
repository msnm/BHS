package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.dto.DTOtoEO;
import be.kdg.bhs.organizer.dto.RouteDTO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;
import be.kdg.bhs.organizer.exceptions.ConveyorServiceException;
import be.kdg.bhs.organizer.exceptions.FlightServiceException;
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
        //Activating the messageConsumer to consume from the Queue, which on his turn callbacks the onReceive and onException methods of this class.
        this.messageConsumerService.initialize(this,messageFormatterService);
    }

    /**
     * A procedural method, executing businesslogic to process incoming suitcases.
     * @param messageDTO the message converted from the wire format to a DTO.
     */
    @Override
    public void onReceive(SuitcaseMessageDTO messageDTO) {
        logger.info("Entered onReceive(): Suitcase {} is being processed", messageDTO.getId());

        //1. Transforming an incoming SuitcaseMessageDTO to SuitCase object
        Suitcase suitcase = DTOtoEO.suitCaseDTOtoEO(messageDTO);

        //2. Retrieving the boarding gate by calling the flightService.
        if(flightService!=null) {
            try {
                suitcase.setBoardingConveyorId(flightService.flightInFormation(suitcase.getFlightNumber()));


            }
            catch (FlightServiceException e) {
                logger.error("Unexpected error during call of flightservice: {}",e.getMessage());
                //TODO Indien de flightservice niet bereikbaar is of een fout teruggeeft wordt later opnieuw geprobeerd deze te contacteren voor het betreffende bagagestuk
            }
        }
        else {
            //Supplying an instance of a service is crucial for the BHS, thus it is okey if this crashes the whole application when this is not provided.
            throw new NullPointerException("No implementation for FlightService was instantiated!");
        }

        //3. Retrieving routeinformation from the ConveyorService.
        if(conveyorService!=null) {
            try {
                RouteDTO routes = conveyorService.routeInformation(suitcase.getConveyorId(),suitcase.getBoardingConveyorId());
            }
            catch(ConveyorServiceException e ) {
                logger.error("Unexpected error during call of conveyorservice: {}",e.getMessage());
                //TODO Indien de conveyorservice niet bereikbaar is of een fout teruggeeft wordt later opnieuw geprobeerd deze te contacteren voor het betreffende bagagestuk
            }
        }
        else {
            //Supplying an instance of a service is crucial for the BHS, thus it is okey if this crashes the whole application when this is not provided.
            throw new NullPointerException("No implementation for FlightService was instantiated!");
        }

        //4. Calculating the shortest route
        this.calculateRouteService.shortestRoute();




    }

    @Override
    public void onError() {

    }
}
