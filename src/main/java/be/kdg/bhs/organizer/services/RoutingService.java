package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.dto.*;
import be.kdg.bhs.organizer.exceptions.ConveyorServiceException;
import be.kdg.bhs.organizer.exceptions.FlightServiceException;
import be.kdg.bhs.organizer.model.Route;
import be.kdg.bhs.organizer.model.Routes;
import be.kdg.bhs.organizer.model.SensorMessage;
import be.kdg.bhs.organizer.model.Suitcase;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michael
 * @project BHS
 * Controller of the routing proces for incoming suitcases.
 */
public class RoutingService implements MessageConsumerListener {


    private Logger logger = LoggerFactory.getLogger(RoutingService.class);
    private List<MessageConsumerService> messageConsumerService;
    private MessageProducerService messageProducerService;
    private MessageFormatterService messageFormatterService;
    private FlightService flightService;
    private ConveyorService conveyorService;
    private CalculateRouteService calculateRouteService;

    public RoutingService(List<MessageConsumerService> messageConsumerService, MessageProducerService messageProducerService, MessageFormatterService messageFormatterService, FlightService flightService, ConveyorService conveyorService, CalculateRouteService calculateRouteService) {
        this.messageConsumerService = messageConsumerService;
        this.messageProducerService = messageProducerService;
        this.messageFormatterService = messageFormatterService;
        this.flightService = flightService;
        this.conveyorService = conveyorService;
        this.calculateRouteService = calculateRouteService;
    }

    public void start() {
        //Activating the messageConsumer to consume from the Queues, which on his turn callbacks the onReceive and onException methods of this class.
        //1. Consuming suitcaseMessages
        this.messageConsumerService.get(0).initialize(this,messageFormatterService, SuitcaseMessageDTO.class);
        //2. Consuming sensorMessages
        this.messageConsumerService.get(1).initialize(this,messageFormatterService, SensorMessageDTO.class);
    }

    /**
     * A procedural method, executing businesslogic to process incoming suitcases.
     * @param messageDTO the message converted from the wire format to a DTO.
     */
    @Override
    public void onReceiveSuitcase(SuitcaseMessageDTO messageDTO) {
        logger.info("Entered onReceiveSuitcase(): Suitcase {} is being processed", messageDTO.getId());

        //1. Transforming an incoming SuitcaseMessageDTO to SuitCase object
        Suitcase suitcase = DTOtoEO.suitCaseDTOtoEO(messageDTO);
        Routes routes = null;

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
                RouteDTO routesDTO = conveyorService.routeInformation(suitcase.getConveyorId(),suitcase.getBoardingConveyorId());
                routes = DTOtoEO.routesDTOtoEO(routesDTO);
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
        Route route = this.calculateRouteService.shortestRoute(routes);

        //5. Creating a routeMessage to send to the Simulator
        RouteMessageDTO routeMessageDTO = EOtoDTO.RouteToRouteMessageDTO(route,suitcase);
        messageProducerService.publishMessage(routeMessageDTO,messageFormatterService);
        logger.info("End onReceiveSuitcase(): Suitcase {} ", messageDTO.getId());
    }

    @Override
    public void onReceiveSensorMessage(SensorMessageDTO messageDTO) {

    }

    @Override
    public void onError() {

    }
}
