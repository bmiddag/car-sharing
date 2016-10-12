package objects;

import java.util.EnumMap;
import java.text.SimpleDateFormat;

/**
 * A session to inform potential users of how the system works. Users can join a
 * session with the Inscription class.
 *
 * @see Inscription
 */
public class InfoSession extends DataObject {

    public enum Field implements DataField {

        ID(Integer.class, true, false),
        ADDRESS(Address.class),
        DATE(Long.class),
        OWNERS(Boolean.class),
        PLACES(Integer.class);

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
     * @param address Place the infosession takes place
     * @param date Date of the infosession
     * @param owners Is this a car owner infosession only?
     * @param places Maximum number of attendees
     */
    public InfoSession(Integer id, Address address, Long date, Boolean owners, Integer places) {

        super(new EnumMap<>(Field.class));

        data.put(Field.ID, id);
        data.put(Field.ADDRESS, address);
        data.put(Field.DATE, date);
        data.put(Field.OWNERS, owners);
        data.put(Field.PLACES, places);

    }
    
    

    /**
     * Returns the date of this InfoSession in a format supplied by the format
     * parameter
     *
     * @param format Should be a SimpleDateFormat compliant format.
     * @return The formatted date
     */
    public String getFormattedDate(String format) {
        return new SimpleDateFormat(format).format(getDate());
    }

    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return the address where the infosession will take place
     */
    public Address getAddress() {
        return (Address) data.get(Field.ADDRESS);
    }

    /**
     * @param address the address where the infosession will take place
     */
    public void setAddress(Address address) {
        data.put(Field.ADDRESS, address);
    }

    /**
     * @return Date of the infosession
     */
    public Long getDate() {
        return (Long) data.get(Field.DATE);
    }

    /**
     * @param date Date of the infosession
     */
    public void setDate(Long date) {
        data.put(Field.DATE, date);
    }

    /**
     * @return Maximum number of attendees
     */
    public Boolean getOwners() {
        return (Boolean) data.get(Field.OWNERS);
    }

    /**
     * @param owners Is this an infosession for car owners?
     */
    public void setOwners(Boolean owners) {
        data.put(Field.OWNERS, owners);
    }

    /**
     * @return The maximal capacity of this infosession.
     */
    public Integer getPlaces() {
        return (Integer) data.get(Field.PLACES);
    }

    /**
     * @param places the places to set
     */
    public void setPlaces(Integer places) {
        data.put(Field.PLACES, places);
    }
    
}
