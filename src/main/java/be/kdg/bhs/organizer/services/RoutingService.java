package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.dto.*;
import be.kdg.bhs.organizer.exceptions.ConveyorServiceException;
import be.kdg.bhs.organizer.exceptions.FlightServiceException;
import be.kdg.bhs.organizer.model.Routes;
import be.kdg.bhs.organizer.model.Suitcase;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import be.kdg.bhs.organizer.utils.CacheObject;
import be.kdg.bhs.organizer.utils.InMemoryCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michael
 * @project BHS
 * Controls the routing process for incoming  suitcases and for incoming sensormessages.
 * By implementing the callback interface {@link MessageConsumerListener} it listens to the
 * messageconsumers for suitcases and sensormessages. Besides listening to messageconsumers this class
 * is also responsible for activitating the messageconsumers.
 */
public class RoutingService implements MessageConsumerListener {

    private ConcurrentHashMap<Integer, Integer> conveyorTraffic;
    private Logger logger = LoggerFactory.getLogger(RoutingService.class);
    private List<MessageConsumerService> messageConsumerService;
    private MessageProducerService messageProducerService;
    private MessageFormatterService messageFormatterService;
    private FlightService flightService;
    private ConveyorService conveyorService;
    private CalculateRouteService calculateRouteService;
    private InMemoryCache<Integer,CacheObject<Suitcase>> cacheOfSuitcases;

    public RoutingService(List<MessageConsumerService> messageConsumerService, MessageProducerService messageProducerService,
                          MessageFormatterService messageFormatterService, FlightService flightService,
                          ConveyorService conveyorService, CalculateRouteService calculateRouteService,
                          long expireTimeCacheOfSuitcases, long intervalCacheOfSuitcases) {

        this.messageConsumerService = messageConsumerService;
        this.messageProducerService = messageProducerService;
        this.messageFormatterService = messageFormatterService;
        this.flightService = flightService;
        this.conveyorService = conveyorService;
        this.calculateRouteService = calculateRouteService;
        this.cacheOfSuitcases = new InMemoryCache<>(expireTimeCacheOfSuitcases,intervalCacheOfSuitcases,new InMemoryBehaviourRouteServiceImpl());
        this.conveyorTraffic = new ConcurrentHashMap<>();
        this.calculateRouteService.setConveyorTraffic(this.conveyorTraffic);
    }

    /**
     * Activates messageConsumers to consume from the Queues for suitcases and sensormessages. The messageConsumers on their
     * turn callback the onReceive and onException methods of this class.
     */
    public void start() {
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
        logger.info("Entered onReceiveSuitcase(): Suitcase {} is being processed", messageDTO.getSuitcaseId());

        //1. Transforming an incoming SuitcaseMessageDTO to SuitCase object and storing it in the cacheOfSuitcases
        Suitcase suitcase = DTOtoEO.suitCaseDTOtoEO(messageDTO);

        //2. Processing the suitcase. This logic is shared for sensorMessage and for suitCaseMessage
        this.processSuitcase(suitcase);

        //3. Putting the suitcase in the suitcaseCache to follow it up untill it arrived on the gate.
        this.putSuitcaseInCache(suitcase);

        logger.info("End onReceiveSuitcase(): Suitcase {} ", messageDTO.getSuitcaseId());
    }

    @Override
    public void onReceiveSensorMessage(SensorMessageDTO messageDTO) {
        logger.info("Entered onReceiveSensorMessage(): SensorMessage for suitcase {} is being processed", messageDTO.getBagageID());

        //1. Lookup the suitcase in the suitcaseCache.
        CacheObject<Suitcase> suitcaseCacheObject;
        if ( (suitcaseCacheObject = cacheOfSuitcases.getCacheObject(messageDTO.getBagageID())) != null) {
            Suitcase suitcase = suitcaseCacheObject.getCacheObject();
            suitcase.setConveyorId(messageDTO.getConveyorId());
            this.processSuitcase(suitcase);
        }
        else {
            //TODO: Gooien een error, dat de suitcase niet bestaat. Of kunnen ervoor kiezen om dit gewoon te loggen.
        }

        logger.info("End onReceiveSensorMessage(): SensorMessage for suitcase {}", messageDTO.getBagageID());

    }

    @Override
    public void onError() {

    }

    private void putSuitcaseInCache(Suitcase suitcase) {
        //1. Check if suitcase exits already in inMemoryCache
        if(cacheOfSuitcases.containsCacheObject(suitcase.getId())) {
            logger.error("Suitcase with id {} already exists and is left out!");
        }
        else {
            cacheOfSuitcases.putCacheObject(suitcase.getId(),new CacheObject<>(suitcase));
        }
    }

    private void processSuitcase(Suitcase suitcase) {
        Routes routes = null;

        //1. Retrieving the boarding gate by calling the flightService.
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
        Integer nextConveyorInRoute = this.calculateRouteService.nextConveyorInRoute(routes,suitcase.getConveyorId());

        //5. Creating a routeMessage to send to the Simulator
        RouteMessageDTO routeMessageDTO = EOtoDTO.RouteToRouteMessageDTO(nextConveyorInRoute,suitcase.getId());
        messageProducerService.publishMessage(routeMessageDTO,messageFormatterService);
    }
}
