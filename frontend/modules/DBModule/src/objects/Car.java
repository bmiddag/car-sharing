package objects;

import java.util.EnumMap;

/**
 * A Car class, containing lots of info and state.
 */
public class Car extends DataObject {

    public enum Field implements DataField {
        
    	ID(Integer.class, true, false),
    	NAME(String.class), 
    	PLATE(String.class), 
    	ADDRESS(Address.class), 
    	ZONE(Zone.class), 
    	INSCRIPTION(Integer.class), 
    	OWNER(User.class), 
    	MAKE(String.class), 
    	TYPE(String.class), 
    	MODEL(String.class), 
    	YEAR(Integer.class), 
    	FUEL(String.class), 
    	DESCRIPTION(String.class), 
    	DOORS(Integer.class), 
    	CAPACITY(Integer.class), 
    	TOW(Boolean.class), 
    	GPS(Boolean.class), 
    	CONSUMPTION(Float.class), 
    	CHASSIS(String.class), 
    	VALUE(Integer.class), 
    	MILEAGE(Float.class), 
    	KMPY(Double.class),
        APPROVED(Boolean.class),
        REACHABILITY(String.class),
        AIRCO(Boolean.class),
        KIDDYSEATS(Boolean.class),
        AUTOMATIC(Boolean.class), 
        LARGE(Boolean.class),
        WASTEDISPOSAL(Boolean.class),
        PETS(Boolean.class), 
        SMOKEFREE(Boolean.class),
        CDPLAYER(Boolean.class),
        RADIO(Boolean.class),
        MP3(Boolean.class),
        ACTIVE(Boolean.class),
        TIME(Long.class, false, true),
        EDIT(Long.class, false, true);
        
        private final boolean stamp;
        private final boolean key;
        private final Class type;

        private Field(Class type, boolean key, boolean stamp) {
            this.type = type;
            this.key = key;
            this.stamp = stamp;
        }
        
        private Field(Class type) {
            this.type = type;
            this.key = false;
            this.stamp = false;
        }
        
        @Override
        public Class getType() {
            return type;
        }

        @Override
        public boolean isKey() {
            return key;
        }

        @Override
        public boolean isStamp() {
            return stamp;
        }
    }

    /**
     *
     * @param id Assigned by the DB
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
     * @param time Time entered in the
     * @param edit Last time any info was edited.
     */
    public Car(Integer id, String name, String plate, Address address, Zone zone,
            Integer inscription, User owner, String make, String type, String model,
            Integer year, String fuel, String description, Integer doors,
            Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis,
            Integer value, Float mileage, Double kmpy, Boolean approved, String reachability, 
            Boolean airco, Boolean kiddyseats, Boolean automatic, Boolean large, Boolean wastedisposal,
            Boolean pets, Boolean smokefree, Boolean cdplayer, Boolean radio, Boolean mp3, Boolean active, Long time, Long edit) {

        super(new EnumMap<>(Field.class));

        data.put(Field.ID, id);
        data.put(Field.NAME, name);
        data.put(Field.PLATE, plate);
        data.put(Field.ADDRESS, address);
        data.put(Field.ZONE, zone);
        data.put(Field.INSCRIPTION, inscription);
        data.put(Field.OWNER, owner);
        data.put(Field.MAKE, make);
        data.put(Field.TYPE, type);
        data.put(Field.MODEL, model);
        data.put(Field.YEAR, year);
        data.put(Field.FUEL, fuel);
        data.put(Field.DESCRIPTION, description);
        data.put(Field.DOORS, doors);
        data.put(Field.CAPACITY, capacity);
        data.put(Field.TOW, tow);
        data.put(Field.GPS, gps);
        data.put(Field.CONSUMPTION, consumption);
        data.put(Field.CHASSIS, chassis);
        data.put(Field.VALUE, value);
        data.put(Field.MILEAGE, mileage);
        data.put(Field.KMPY, kmpy);
        data.put(Field.TIME, time);
        data.put(Field.EDIT, edit);
        data.put(Field.APPROVED, approved);
        data.put(Field.REACHABILITY, reachability);
        data.put(Field.AIRCO, airco); 
        data.put(Field.KIDDYSEATS, kiddyseats); 
        data.put(Field.AUTOMATIC, automatic); 
        data.put(Field.LARGE, large); 
        data.put(Field.WASTEDISPOSAL, wastedisposal); 
        data.put(Field.PETS, pets); 
        data.put(Field.SMOKEFREE, smokefree); 
        data.put(Field.CDPLAYER, cdplayer); 
        data.put(Field.RADIO, radio); 
        data.put(Field.MP3, mp3);
        data.put(Field.ACTIVE, active);
        
    }
    
    

    @Override
    public String toString() {
        return getName();
    }
    
    public String getReachability() {
        return (String) data.get(Field.REACHABILITY);
    }

    public void setReachability(String reachability) {
        data.put(Field.REACHABILITY, reachability);
    }

    public Boolean getActive() {
        return (Boolean) data.get(Field.ACTIVE);
    }

    public void setActive(Boolean active) {
        data.put(Field.ACTIVE, active);
    }

    public Boolean getAirco() {
        return (Boolean) data.get(Field.AIRCO);
    }

    public void setAirco(Boolean airco) {
        data.put(Field.AIRCO, airco);
    }

    public Boolean getKiddyseats() {
        return (Boolean) data.get(Field.KIDDYSEATS);
    }

    public void setKiddyseats(Boolean kiddyseats) {
        data.put(Field.KIDDYSEATS, kiddyseats);
    }

    public Boolean getAutomatic() {
        return (Boolean) data.get(Field.AUTOMATIC);
    }

    public void setAutomatic(Boolean automatic) {
        data.put(Field.AUTOMATIC, automatic);
    }

    public Boolean getLarge() {
        return (Boolean) data.get(Field.LARGE);
    }

    public void setLarge(Boolean large) {
        data.put(Field.LARGE, large);
    }

    public Boolean getWastedisposal() {
        return (Boolean) data.get(Field.WASTEDISPOSAL);
    }

    public void setWastedisposal(Boolean wastedisposal) {
        data.put(Field.WASTEDISPOSAL, wastedisposal);
    }

    public Boolean getPets() {
        return (Boolean) data.get(Field.PETS);
    }

    public void setPets(Boolean pets) {
        data.put(Field.PETS, pets);
    }

    public Boolean getSmokefree() {
        return (Boolean) data.get(Field.SMOKEFREE);
    }

    public void setSmokefree(Boolean smokefree) {
        data.put(Field.SMOKEFREE, smokefree);
    }

    public Boolean getCdplayer() {
        return (Boolean) data.get(Field.CDPLAYER);
    }

    public void setCdplayer(Boolean cdplayer) {
        data.put(Field.CDPLAYER, cdplayer);
    }

    public Boolean getRadio() {
        return (Boolean) data.get(Field.RADIO);
    }

    public void setRadio(Boolean radio) {
        data.put(Field.RADIO, radio);
    }

    public Boolean getMp3() {
        return (Boolean) data.get(Field.MP3);
    }

    public void setMp3(Boolean mp3) {
        data.put(Field.MP3, mp3);
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return the owner
     */
    public User getOwner() {
        return (User) data.get(Field.OWNER);
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(User owner) {
        data.put(Field.OWNER, owner);
    }

    /**
     * @return the name
     */
    public String getName() {
        return (String) data.get(Field.NAME);
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        data.put(Field.NAME, name);
    }

    /**
     * @return the plate
     */
    public String getPlate() {
        return (String) data.get(Field.PLATE);
    }

    /**
     * @param plate the plate to set
     */
    public void setPlate(String plate) {
        data.put(Field.PLATE, plate);
    }

    /**
     * @return the address where this car can usually be found
     */
    public Address getAddress() {
        return (Address) data.get(Field.ADDRESS);
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        data.put(Field.ADDRESS, address);
    }

    /**
     * @return the this car belongs to
     */
    public Zone getZone() {
        return (Zone) data.get(Field.ZONE);
    }

    /**
     * @param zone the zone this car belongs to
     */
    public void setZone(Zone zone) {
        data.put(Field.ZONE, zone);
    }

    /**
     * @return the inscription
     */
    public Integer getInscription() {
        return (Integer) data.get(Field.INSCRIPTION);
    }

    /**
     * @param inscription the inscription to set
     */
    public void setInscription(Integer inscription) {
        data.put(Field.INSCRIPTION, inscription);
    }

    /**
     * @return the make
     */
    public String getMake() {
        return (String) data.get(Field.MAKE);
    }

    /**
     * @param make the make to set
     */
    public void setMake(String make) {
        data.put(Field.MAKE, make);
    }

    /**
     * @return the car type
     */
    public String getType() {
        return (String) data.get(Field.TYPE);
    }

    /**
     * @param type the car type to set
     */
    public void setType(String type) {
        data.put(Field.TYPE, type);
    }

    /**
     * @return the car model
     */
    public String getModel() {
        return (String) data.get(Field.MODEL);
    }

    /**
     * @param model the car model to set
     */
    public void setModel(String model) {
        data.put(Field.MODEL, model);
    }

    /**
     * @return the construction year
     */
    public Integer getYear() {
        return (Integer) data.get(Field.YEAR);
    }

    /**
     * @param year the construction year to set
     */
    public void setYear(Integer year) {
        data.put(Field.YEAR, year);
    }

    /**
     * @return the fuel type
     */
    public String getFuel() {
        return (String) data.get(Field.FUEL);
    }

    /**
     * @param fuel the fuel type to set
     */
    public void setFuel(String fuel) {
        data.put(Field.FUEL, fuel);
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return (String) data.get(Field.DESCRIPTION);
    }

    /**
     * @param description the description of the car to set
     */
    public void setDescription(String description) {
        data.put(Field.DESCRIPTION, description);
    }

    /**
     * @return the number of doors
     */
    public Integer getDoors() {
        return (Integer) data.get(Field.DOORS);
    }

    /**
     * @param doors the number of doors to set
     */
    public void setDoors(Integer doors) {
        data.put(Field.DOORS, doors);
    }

    /**
     * @return the capacity
     */
    public Integer getCapacity() {
        return (Integer) data.get(Field.CAPACITY);
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Integer capacity) {
        data.put(Field.CAPACITY, capacity);
    }

    /**
     * @return tow available?
     */
    public Boolean getApproved() {
        return (Boolean) data.get(Field.APPROVED);
    }

    /**
     * @param tow tow available?
     */
    public void setApproved(Boolean approved) {
        data.put(Field.APPROVED, approved);
    }

    /**
     * @return tow available?
     */
    public Boolean getTow() {
        return (Boolean) data.get(Field.TOW);
    }

    /**
     * @param tow tow available?
     */
    public void setTow(Boolean tow) {
        data.put(Field.TOW, tow);
    }

    /**
     * @return gps available?
     */
    public Boolean getGps() {
        return (Boolean) data.get(Field.GPS);
    }

    /**
     * @param gps gps available?
     */
    public void setGps(Boolean gps) {
        data.put(Field.GPS, gps);
    }

    /**
     * @return the fuel consumption per 100km
     */
    public Float getConsumption() {
        return (Float) data.get(Field.CONSUMPTION);
    }

    /**
     * @param consumption the fuel consumption per 100km to set
     */
    public void setConsumption(Float consumption) {
        data.put(Field.CONSUMPTION, consumption);
    }

    /**
     * @return the chassis number
     */
    public String getChassis() {
        return (String) data.get(Field.CHASSIS);
    }

    /**
     * @param chassis the chassis number to set
     */
    public void setChassis(String chassis) {
        data.put(Field.CHASSIS, chassis);
    }

    /**
     * @return the estimated value
     */
    public Integer getValue() {
        return (Integer) data.get(Field.VALUE);
    }

    /**
     * @param value the value to set
     */
    public void setValue(Integer value) {
        data.put(Field.VALUE, value);
    }

    /**
     * @return the mileage
     */
    public Float getMileage() {
        return (Float) data.get(Field.MILEAGE);
    }

    /**
     * @param mileage the mileage to set
     */
    public void setMileage(Float mileage) {
        data.put(Field.MILEAGE, mileage);
    }

    /**
     * @return the average kilometre per year
     */
    public Double getKmpy() {
        return (Double) data.get(Field.KMPY);
    }

    /**
     * @param kmpy the average kilometre per year to set
     */
    public void setKmpy(Double kmpy) {
        data.put(Field.KMPY, kmpy);
    }

    /**
     * @return the date the car was added to the DB
     */
    public Long getTime() {
        return (Long) data.get(Field.TIME);
    }

    /**
     * @return The last time this car was edited
     */
    public Long getEdit() {
        return (Long) data.get(Field.EDIT);
    }
    
}
