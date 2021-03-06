package objects;

import java.util.EnumMap;

/**
 * The reservation of a Car by a User for a certain time.
 */
public class ReservationRange extends DataObject {

    public enum Day {
        NODAY, SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
    }
    
    public enum Field implements DataField {

	ID(Integer.class, true, false),
	CAR(Car.class), 
	BEGIN(Long.class), 
	END(Long.class), 
	DAY(Integer.class);

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
     * @param id the id generated by the db
     * @param day the day for which this limit counts
     * @param car the car
     * @param begin the start of the reservation range
     * @param end the end of the reservation range
     */
    public ReservationRange(Integer id, Car car, Long begin, Long end, Integer day) {

        super(new EnumMap<>(Field.class));
        data.put(Field.ID, id);
        data.put(Field.CAR, car);
        data.put(Field.BEGIN, begin);
        data.put(Field.END, end);
        data.put(Field.DAY, day);

    }
    
    

    /**
     *
     * @return the id assigned by the db
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     *
     * @return the id of the car
     */
    public Integer getCarID() {
        Car car = (Car) data.get(Field.CAR);
        return car == null ? null : car.getId();
    } 
    
    /**
     *
     * @return the car
     * @throws exceptions.DataAccessException
     */
    public Car getCar() {
        return (Car) data.get(Field.CAR);
    }

    /**
     *
     * @return the start of the reservation
     */
    public Long getBegin() {
        return (Long) data.get(Field.BEGIN);
    }

    /**
     *
     * @return the end of th reservation
     */
    public Long getEnd() {
        return (Long) data.get(Field.END);
    }

    /**
     *
     * @param begin the start of the reservation
     */
    public void setBegin(Long begin) {
        data.put(Field.BEGIN, begin);
    }

    /**
     *
     * @param end the end of the reservation
     */
    public void setEnd(Long end) {
        data.put(Field.END, end);
    }

    /**
     *
     * @param day set the day of this range
     */
    public void setDay(Day day) {
        data.put(Field.DAY, day.ordinal());
    }

    /**
     *
     * @return 
     */
    public Day getDay() {
        return (Day) Day.values()[(int)data.get(Field.DAY)];
    }

}
