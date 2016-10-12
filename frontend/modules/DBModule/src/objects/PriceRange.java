package objects;

import java.util.EnumMap;

/**
 * An indication of the price/km in a range of km from min to max.
 */
public class PriceRange extends DataObject {

    public enum Field implements DataObject.DataField {
	
	ID(Integer.class, true, false),
	MIN(Integer.class), 
	MAX(Integer.class), 
	PRICE(Integer.class);

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
     * @param id the id assigned by the db
     * @param min the lower bound of the km range
     * @param max the upper bound of the km range
     * @param price the km price used in this range
     */
    public PriceRange(Integer id, Integer min, Integer max, Integer price) {
        super(new EnumMap<>(Field.class));
        data.put(Field.ID, id);
        data.put(Field.MIN, min);
        data.put(Field.MAX, max);
        data.put(Field.PRICE, price);
    }
    
    

    /**
     *
     * @return the id generated by the db
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     *
     * @return the lower bound of the range
     */
    public Integer getMin() {
        return (Integer) data.get(Field.MIN);
    }

    /**
     *
     * @return the upper bound of the range
     */
    public Integer getMax() {
        return (Integer) data.get(Field.MAX);
    }

    /**
     *
     * @return the price per km in this range
     */
    public Integer getPrice() {
        return (Integer) data.get(Field.PRICE);
    }

    /**
     *
     * @param min the lower bound
     */
    public void setMin(Integer min) {
        data.put(Field.MIN, min);
    }

    /**
     *
     * @param max the upper bound
     */
    public void setMax(Integer max) {
        data.put(Field.MAX, max);
    }

    /**
     *
     * @param price the price used in this range
     */
    public void setPrice(Integer price) {
        data.put(Field.PRICE, price);
    }

}
