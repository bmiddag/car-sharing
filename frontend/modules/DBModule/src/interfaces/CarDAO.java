package interfaces;

import java.util.List;

import objects.Car;
import objects.User;

import exceptions.DataAccessException;
import objects.Address;
import objects.Zone;

public interface CarDAO extends DataAccessObject<Car> {
    
    public class PossibleReservation {
        private final Car car;
        private final Long begin;
        private final Long end;
        public PossibleReservation(Car car, Long begin, Long end) {
            this.car = car;
            this.begin = begin;
            this.end = end;
        }
        public Car getCar() {
            return car;
        }
        public Long getBegin() {
            return begin;
        }
        public Long getEnd() {
            return end;
        }
    }

    /**
     * Creates a new Car with these specific parameters.
     *
     * @param name
     * @param plate license plate
     * @param address Where can this car be found?
     * @param zone The Zone this car belongs to.
     * @param inscription
     * @param owner
     * @param make
     * @param type
     * @param model
     * @param year Year the car was built
     * @param fuel The type of fuel, diesel, petrol, hybrid, ...
     * @param description A brief description
     * @param doors Number of doors
     * @param capacity Number of seats
     * @param tow
     * @param gps
     * @param consumption Average fuel consumption per 100km
     * @param chassis
     * @param value Estimated value
     * @param mileage Current mileage of the car
     * @param kmpy Average kilometres per year driven.
     * @return a new object.Car object
     * @throws exceptions.DataAccessException
     */
    public Car createCar(User owner, String name, String plate, Address address, Zone zone,
            Integer inscription, String make, String type, String model,
            Integer year, String fuel, String description, Integer doors,
            Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis,
            Integer value, Float mileage, Double kmpy) throws DataAccessException;
    
    
    /**
     * Creates a new Car with these specific parameters.
     *
     * @param name
     * @param plate license plate
     * @param address Where can this car be found?
     * @param approved if the car information is approved by an admin
     * @param zone The Zone this car belongs to.
     * @param inscription
     * @param owner
     * @param make
     * @param type
     * @param model
     * @param year Year the car was built
     * @param fuel The type of fuel, diesel, petrol, hybrid, ...
     * @param description A brief description
     * @param doors Number of doors
     * @param capacity Number of seats
     * @param tow
     * @param gps
     * @param consumption Average fuel consumption per 100km
     * @param chassis
     * @param value Estimated value
     * @param mileage Current mileage of the car
     * @param kmpy Average kilometres per year driven.
     * @return a new object.Car object
     * @throws exceptions.DataAccessException
     */
    public Car createCar(User owner, String name, String plate, Boolean approved, Address address, Zone zone,
            Integer inscription, String make, String type, String model,
            Integer year, String fuel, String description, Integer doors,
            Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis,
            Integer value, Float mileage, Double kmpy) throws DataAccessException;

    
    /**
     * Creates a new Car with these specific parameters.
     *
     * @param name
     * @param plate license plate
     * @param address Where can this car be found?
     * @param approved if the car information is approved by an admin
     * @param zone The Zone this car belongs to.
     * @param inscription
     * @param owner
     * @param make
     * @param type
     * @param model
     * @param year Year the car was built
     * @param fuel The type of fuel, diesel, petrol, hybrid, ...
     * @param description A brief description
     * @param doors Number of doors
     * @param capacity Number of seats
     * @param tow
     * @param gps
     * @param consumption Average fuel consumption per 100km
     * @param chassis
     * @param value Estimated value
     * @param mileage Current mileage of the car
     * @param kmpy Average kilometres per year driven.
     * @return a new object.Car object
     * @throws exceptions.DataAccessException
     */
    public Car createCar(User owner, String name, String plate, Boolean approved, Address address, Zone zone,
            Integer inscription, String make, String type, String model,
            Integer year, String fuel, String description, Integer doors,
            Integer capacity, String reachability, Boolean tow, Boolean gps, 
            Boolean airco, Boolean kiddyseats, Boolean automatic, Boolean large, Boolean wastedisposal,
            Boolean pets, Boolean smokefree, Boolean cdplayer, Boolean radio, Boolean mp3, Float consumption, String chassis,
            Integer value, Float mileage, Double kmpy, Boolean active) throws DataAccessException;
    
    /**
     * Creates a new Car with these specific parameters.
     *
     * @see objects.Car
     * @param owner Parameter for the new Car
     * @param name Parameter for the new Car, a given name, should be unique and
     * descriptive
     * @param plate Parameter for the new Car
     * @return A new objects.Car object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Car createCar(User owner, String name, String plate) throws DataAccessException;

    /**
     * Returns the Car with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param id The id of the Car to get from the DB
     * @return A new objects.Car object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Car getCar(Integer id) throws DataAccessException;

    /**
     * Returns all Cars that belong to this User. Usually just one.
     *
     * @param user
     * @return A new objects.Car object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Car> getCars(User user) throws DataAccessException;

    /**
     * Returns all Cars that belong to this User. Usually just one.
     *
     * @param user
     * @return A new objects.Car object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Car> getCars(Integer user) throws DataAccessException;

    /**
     * Returns all the cars in the DB.
     *
     * @return All Cars in the DB
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Car> getCars() throws DataAccessException;

    /**
     * Returns all the cars that satisfy the given filter.
     *
     * @param  filter the filter to satisfy
     * @return all the cars that satisfy the given filter
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Car> getCars(Filter<Car> filter) throws DataAccessException;
    
    /*
    public List<Car> getAvailableCars(Long from, Long to, Long duration) throws DataAccessException;
    public List<Car> getAvailableCars(Long from, Long to) throws DataAccessException;
    public List<Car> getAvailableCars(Long from, Long to, Filter<Car> filter) throws DataAccessException;
    */
    
    public List<PossibleReservation> getAvailableCars(Long from, Long to, Long duration, Filter<Car> filter) throws DataAccessException;
    
    /**
     * Writes all fields of this Car to the DB.
     *
     * @param car the Car to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateCar(Car car) throws DataAccessException;
    
    public void updateCar(Integer id, Integer owner, String name, String plate, Integer address, Integer zone, Integer inscription, String make, String type, String model, Integer year, String fuel, String description, Integer doors, Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis, Integer value, Float mileage, Double kmpy, Boolean approved, String reachability, Boolean airco, Boolean kiddyseats, Boolean automatic, Boolean large, Boolean wastedisposal, Boolean pets, Boolean smokefree, Boolean cdplayer, Boolean radio, Boolean mp3, Boolean active) throws DataAccessException;

    /**
     *
     * @param id the id of the car
     * @param name the name of the car
     * @param plate license plate
     * @param address Where can this car be found?
     * @param zone The Zone this car belongs to.
     * @param inscription
     * @param owner
     * @param make
     * @param type
     * @param model
     * @param year Year the car was built
     * @param fuel The type of fuel, diesel, petrol, hybrid, ...
     * @param description A brief description
     * @param doors Number of doors
     * @param capacity Number of seats
     * @param tow
     * @param gps
     * @param consumption Average fuel consumption per 100km
     * @param chassis
     * @param value Estimated value
     * @param mileage Current mileage of the car
     * @param kmpy Average kilometres per year driven.
     * @param approved
     * @throws exceptions.DataAccessException
     */
    public void updateCar(Integer id, Integer owner, String name, String plate, Integer address, Integer zone,
            Integer inscription, String make, String type, String model,
            Integer year, String fuel, String description, Integer doors,
            Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis,
            Integer value, Float mileage, Double kmpy, Boolean approved) throws DataAccessException;
    
    /**
     *
     * @param id the id of the car
     * @param name the name of the car
     * @param plate license plate
     * @param address Where can this car be found?
     * @param zone The Zone this car belongs to.
     * @param inscription
     * @param owner
     * @param make
     * @param type
     * @param model
     * @param year Year the car was built
     * @param fuel The type of fuel, diesel, petrol, hybrid, ...
     * @param description A brief description
     * @param doors Number of doors
     * @param capacity Number of seats
     * @param tow
     * @param gps
     * @param consumption Average fuel consumption per 100km
     * @param chassis
     * @param value Estimated value
     * @param mileage Current mileage of the car
     * @param kmpy Average kilometres per year driven.
     * @throws exceptions.DataAccessException
     */
    public void updateCar(Integer id, Integer owner, String name, String plate, Integer address, Integer zone,
            Integer inscription, String make, String type, String model,
            Integer year, String fuel, String description, Integer doors,
            Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis,
            Integer value, Float mileage, Double kmpy) throws DataAccessException;

    /**
     * Deletes the specified Car.
     *
     * @param car The Car to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteCar(Car car) throws DataAccessException;


    /**
     * Deletes the specified Car.
     *
     * @param car The Car to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteCar(Integer car) throws DataAccessException;

    public List<Car> searchCar(String query) throws DataAccessException;

    /**
     * Returns a car that belongs to the user given his id.
     *
     * @param id the id of the user
     * @return the car belonging to the given user
     * @throws DataAccessException
     */
    public Car getCarByUserId(Integer id) throws DataAccessException;

}
