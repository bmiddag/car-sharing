package objects;

import java.util.EnumMap;
import objects.DataObject.DataField;

/**
 * An address class.
 */
public class Address extends DataObject/*<Address.Field>*/ {

    public enum Field implements DataField {
	
	ID(Integer.class, true, false), 
	STREET(String.class), 
	NUMBER(Integer.class), 
	BUS(String.class),
	PLACE(Place.class);

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
     * @param place The city this address is in
     * @param street
     * @param number
     * @param bus Extra bus information
     */
    public Address(Integer id, String street, Integer number, String bus, Place place) {
        super(new EnumMap<>(Field.class)); // super(Field.class);
        data.put(Field.ID, id);
        data.put(Field.PLACE, place);
        data.put(Field.STREET, street);
        data.put(Field.NUMBER, number);
        data.put(Field.BUS, bus);
    }
    

    public String getFormattedAddress() {
        return new StringBuilder()
                .append(getStreet())
                .append(" ")
                .append(getNumber())
                .append(getBus() != null && !((String)getData(Field.BUS)).isEmpty() ? " (bus " + getBus() + ")" : "")
                .append(", ")
                .append(getPlace().getZip())
                .append(" ")
                .append(getPlace().getName())
                .toString();
    }
    
    

    @Override
    public String toString() {
        return getFormattedAddress();
    }

    /**
     * @return the id assigned by the DB
     */
    public Integer getId() {
        return getData(Field.ID);
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return getData(Field.STREET);
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        data.put(Field.STREET, street);
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return getData(Field.NUMBER);
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        data.put(Field.NUMBER, number);
    }

    /**
     * @return the bus
     */
    public String getBus() {
        return getData(Field.BUS);
    }

    /**
     * @param bus the bus to set
     */
    public void setBus(String bus) {
        data.put(Field.BUS, bus);
    }

    /**
     * @return the place
     */
    public Place getPlace() {
        return getData(Field.PLACE);
    }

    /**
     * @return the place
     */
    public Integer getPlaceID() {
        return getData(Field.PLACE) == null ? null : ((Place)getData(Field.PLACE)).getId();
    }

    /**
     * @param place the place to set
     */
    public void setPlace(Place place) {
        data.put(Field.PLACE, place);
    }
}
