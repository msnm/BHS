package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.dto.*;
import be.kdg.bhs.organizer.exceptions.ConveyorServiceException;
import be.kdg.bhs.organizer.exceptions.FlightServiceException;
import be.kdg.bhs.organizer.exceptions.RoutingException;
import be.kdg.bhs.organizer.model.Routes;
import be.kdg.bhs.organizer.model.Status;
import be.kdg.bhs.organizer.model.StatusMessage;
import be.kdg.bhs.organizer.model.Suitcase;
import be.kdg.bhs.organizer.repo.CacheObject;
import be.kdg.bhs.organizer.repo.InMemoryCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael
 * @project BHS
 * Controls the routing process for incoming  suitcases and for incoming sensormessages.
 * By implementing the callback interface {@link MessageConsumerListener} it listens to the
 * messageconsumers for suitcases and sensormessages. Besides listening to messageconsumers this class
 * is also responsible for activating the messageproducers and thus sending statusmessages and routingmessages.
 */
public class RoutingService implements MessageConsumerListener {

    private ConcurrentHashMap<Integer, Integer> conveyorTraffic;
    private Logger logger = LoggerFactory.getLogger(RoutingService.class);
    private List<MessageConsumerService> messageConsumerServices;
    private List<MessageProducerService> messageProducerServices;
    private MessageFormatterService messageFormatterService;
    private FlightService flightService;
    private ConveyorService conveyorService;
    private CalculateRouteService calculateRouteService;
    private InMemoryCache<Integer,CacheObject<Suitcase>> cacheOfSuitcases;
    private int recursiveCount = 1;

    public RoutingService(List<MessageConsumerService> messageConsumerServices, List<MessageProducerService> messageProducerServices,
                          MessageFormatterService messageFormatterService, FlightService flightService,
                          ConveyorService conveyorService, CalculateRouteService calculateRouteService,
                          long expireTimeCacheOfSuitcases, long intervalCacheOfSuitcases) {

        this.messageConsumerServices = messageConsumerServices;
        this.messageProducerServices = messageProducerServices;
        this.messageFormatterService = messageFormatterService;
        this.flightService = flightService;
        this.conveyorService = conveyorService;
        this.calculateRouteService = calculateRouteService;
        this.cacheOfSuitcases = new InMemoryCache<>(expireTimeCacheOfSuitcases,intervalCacheOfSuitcases,new InMemoryBehaviourRouteServiceImpl(this));
        this.conveyorTraffic = new ConcurrentHashMap<>();
        this.calculateRouteService.setConveyorTraffic(this.conveyorTraffic);
    }

    /**
     * Activates messageConsumers to consume from the Queues for suitcases and sensormessages. The messageConsumers on their
     * turn callback the onReceive and onException methods of this class.
     */
    public void start() {
        //1. Consuming suitcaseMessages
        this.messageConsumerServices.get(0).initialize(this,messageFormatterService, SuitcaseMessageDTO.class);
        //2. Consuming sensorMessages
        this.messageConsumerServices.get(1).initialize(this,messageFormatterService, SensorMessageDTO.class);
    }

    /**
     * A procedural method, executing businesslogic to process incoming suitcases.
     * @param messageDTO the message converted from the wire format to a DTO.
     */
    @Override
    public void onReceiveSuitcaseMessage(SuitcaseMessageDTO messageDTO) {
        logger.debug("Entered: onReceiveSuitcaseMessage(): Suitcase {} is being processed", messageDTO.getSuitcaseId());

        //1. Transforming an incoming SuitcaseMessageDTO to SuitCase object and storing it in the cacheOfSuitcases
        Suitcase suitcase = DTOtoEO.suitCaseDTOtoEO(messageDTO);

        //2. Processing the suitcase. This logic is shared for sensorMessage and for suitCaseMessage
        try {
            this.processSuitcase(suitcase);
            //3. Putting the suitcase in the suitcaseCache to follow it up until it is arrived on the gate.
            this.putSuitcaseInCache(suitcase);
        } catch (RoutingException e) {
            logger.error("Error while processing suitcase with id {} and errormessage {}",suitcase.getSuitCaseId(),e.getMessage());
            this.sendStatusMessage(new StatusMessage(Status.UNDELIVERABLE,suitcase.getSuitCaseId(),suitcase.getConveyorId()));
        }

        logger.debug("End: onReceiveSuitcaseMessage(): Suitcase {} {}", messageDTO.getSuitcaseId(),"\n");
    }

    @Override
    public void onReceiveSensorMessage(SensorMessageDTO messageDTO) {
        logger.debug("Entered: onReceiveSensorMessage(): SensorMessage for suitcase {} is being processed", messageDTO.getSuitcaseId());

        //1. Lookup the suitcase in the suitcaseCache.
        CacheObject<Suitcase> suitcaseCacheObject;
        if ((suitcaseCacheObject = cacheOfSuitcases.getCacheObject(messageDTO.getSuitcaseId())) != null) {
            Suitcase suitcase = suitcaseCacheObject.getCacheObject();
            suitcase.setConveyorId(messageDTO.getConveyorId());
            suitcaseCacheObject.setTimeEnteredCache(System.currentTimeMillis()); //Need to modify the time timeEntered in cache, because otherwise this object will expire, while it has to stay in the cache!

            if (suitcase.getBoardingConveyorId().compareTo(suitcase.getConveyorId())==0) {
                //When the boardingconveyor and the currentconveyor are the same the suitcase is at its end destination.
                //Need to send arrive message and remove the suitcase from the inmemorycache.
                this.sendStatusMessage(new StatusMessage(Status.ARRIVED,suitcase.getSuitCaseId(),suitcase.getConveyorId()));
                cacheOfSuitcases.removeCacheObject(suitcase.getSuitCaseId());
            }
            else {
                try {
                    this.processSuitcase(suitcase);
                } catch (RoutingException e) {
                    logger.error("Error while processing suitcase with id {} and errormessage",suitcase.getSuitCaseId(),e.getMessage());
                    this.sendStatusMessage(new StatusMessage(Status.UNDELIVERABLE,suitcase.getSuitCaseId(),suitcase.getConveyorId()));
                }
            }

        }
        else {
            //The suitcase does not exist in the inMemoryCache so a statusMessage LOST is send.
            this.sendStatusMessage(new StatusMessage(Status.LOST,messageDTO.getSuitcaseId(),messageDTO.getConveyorId()));
        }
        logger.debug("End: onReceiveSensorMessage(): SensorMessage for suitcase {} {}", messageDTO.getSuitcaseId(),"\n");

    }

    @Override
    public void sendStatusMessage(StatusMessage statusMessage) {
        logger.debug("Entered: sendStatusMessage({})",statusMessage.toString());

        if (statusMessage.getStatus().equals(Status.UNDELIVERABLE)) {
            cacheOfSuitcases.removeCacheObject(statusMessage.getSuitcaseId());
        }
        messageProducerServices.get(0).publishMessage(EOtoDTO.StatusMessageToStatusMessageDTO(statusMessage),messageFormatterService);
        logger.debug("End: sendStatusMessage({}) {}",statusMessage.toString(),"\n");
    }

    private void putSuitcaseInCache(Suitcase suitcase) {
        //1. Check if suitcase exits already in inMemoryCache
        if(cacheOfSuitcases.containsCacheObject(suitcase.getSuitCaseId())) {
            logger.error("Suitcase with id {} already exists and is left out!");
        }
        else {
            cacheOfSuitcases.putCacheObject(suitcase.getSuitCaseId(),new CacheObject<>(suitcase));
        }
    }

    private void processSuitcase(Suitcase suitcase) throws RoutingException {
        Routes routes = null;

        //1. Retrieving the boarding gate by calling the flightService.
        consultFlightService(suitcase);

        //3. Retrieving routeinformation from the ConveyorService.
        routes = consultConveyorService(suitcase, routes);

        //4. Calculating the shortest route
        Integer nextConveyorInRoute = this.calculateRouteService.nextConveyorInRoute(routes,suitcase.getConveyorId());

        //5. Creating a routeMessage to send to the Simulator
        RouteMessageDTO routeMessageDTO = EOtoDTO.RouteToRouteMessageDTO(nextConveyorInRoute,suitcase.getSuitCaseId());
        messageProducerServices.get(0).publishMessage(routeMessageDTO,messageFormatterService);
    }

    private Routes consultConveyorService(Suitcase suitcase, Routes routes) throws RoutingException {
        if(conveyorService!=null) {
            try {
                RouteDTO routesDTO = conveyorService.routeInformation(suitcase.getConveyorId(),suitcase.getBoardingConveyorId());
                routes = DTOtoEO.routesDTOtoEO(routesDTO);
            }
            catch(ConveyorServiceException e ) {
                logger.error("Unexpected error during call of conveyorservice: {}. Attempting a recursive call for suitcase {}",e.getMessage(),suitcase.getSuitCaseId());
                if (recursiveCount++<2) {
                    consultConveyorService(suitcase,routes); //Indien de conveyorservice niet bereikbaar is of een fout teruggeeft wordt later opnieuw geprobeerd deze te contacteren voor het betreffende bagagestuk
                }
                else {
                    recursiveCount=0;
                    //this.sendStatusMessage();
                    throw new RoutingException(e.getMessage());
                }
            }
        }
        else {
            //Supplying an instance of a service is crucial for the BHS, thus it is okey if this crashes the whole application when this is not provided.
            throw new NullPointerException("No implementation for FlightService was instantiated!");
        }
        return routes;
    }

    private void consultFlightService(Suitcase suitcase) throws RoutingException {

        if(flightService!=null) {
            try {
                suitcase.setBoardingConveyorId(flightService.flightInFormation(suitcase.getFlightNumber()));


            }
            catch (FlightServiceException e) {
                logger.error("Unexpected error during call of flightservice: {}.Attempting a recursive call for suitcase {}",e.getMessage(),suitcase.getSuitCaseId());
                if (recursiveCount++<2) {
                    consultFlightService(suitcase); //Indien de flightservice niet bereikbaar is of een fout teruggeeft wordt later opnieuw geprobeerd deze te contacteren voor het betreffende bagagestuk
                }
                else {
                    recursiveCount=0;
                    throw new RoutingException(e.getMessage());
                }
            }
        }
        else {
            //Supplying an instance of a service is crucial for the BHS, thus it is okey if this crashes the whole application when this is not provided.
            throw new NullPointerException("No implementation for FlightService was instantiated!");
        }
    }
}
