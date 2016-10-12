package objects;

import java.util.EnumMap;

/**
 * A (repeated) action for internal use.
 */
public class Action extends DataObject {

    public enum Field implements DataField {
	
	ID(Integer.class, true, false),
	NAME(String.class), 
	START(Long.class), 
	PERIOD(Long.class);

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

    //private final Map<Field, Object> data = new EnumMap<>(Field.class);

    /**
     *
     * @param id assigned by the DB
     * @param name This action's name
     * @param start The moment when the first action should be taken
     * @param period The amount of seconds between repetitions
     */
    public Action(Integer id, String name, Long start, Long period) {
        super(new EnumMap<>(Field.class));
        data.put(Field.ID, id);
        data.put(Field.NAME, name);
        data.put(Field.START, start);
        data.put(Field.PERIOD, period);
    }
    
    

    /**
     * @return the id assigned by the DB
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return the name of the action
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
     * @return the start of this action
     */
    public Long getStart() {
        return (Long) data.get(Field.START);
    }

    /**
     * @param start set the start of this action
     */
    public void setStart(long start) {
        data.put(Field.START, start);
    }

    /**
     * @return The time in seconds between two actions
     */
    public Long getPeriod() {
        return (Long) data.get(Field.PERIOD);
    }

    /**
     * @param period The time in seconds between two actions
     */
    public void setPeriod(Long period) {
        data.put(Field.PERIOD, period);
    }
    
}
