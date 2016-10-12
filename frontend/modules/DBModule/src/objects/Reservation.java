package objects;

import java.util.EnumMap;

/**
 * The reservation of a Car by a User for a certain time.
 */
public class Reservation extends DataObject {

    public enum Field implements DataField {

	ID(Integer.class, true, false),
	USER(User.class), 
	CAR(Car.class), 
	BEGIN(Long.class), 
	END(Long.class), 
	APPROVED(Boolean.class), 
	PENDING(Boolean.class),
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
     * @param id the id generated by the db
     * @param user the user reserving a car
     * @param car the reserved car
     * @param begin the start of the reservation
     * @param end the end of the reservation
     * @param approved if the owner already approved the reservation
     * @param pending if an administrator already checked the reservation
     * @param time the time a reservation was entered in the system
     * @param edit the last time this reservation was edited
     */
    public Reservation(Integer id, User user, Car car, Long begin, Long end,
            Boolean approved, Boolean pending, Long time, Long edit) {

        super(new EnumMap<>(Field.class));
        data.put(Field.ID, id);
        data.put(Field.USER, user);
        data.put(Field.CAR, car);
        data.put(Field.BEGIN, begin);
        data.put(Field.END, end);
        data.put(Field.APPROVED, approved);
        data.put(Field.PENDING, pending);
        data.put(Field.TIME, time);
        data.put(Field.EDIT, edit);

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
     * @return the user who made the reservation
     * @throws exceptions.DataAccessException
     */
    public User getUser() {
        return (User) data.get(Field.USER);
    }
    
    /**
     *
     * @return the id of the user making the reservation
     */
    public Integer getUserID() {
        User user = (User) data.get(Field.USER);
        return user == null ? null : user.getId();
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
     * @return true if the owner approved of the reservation
     */
    public Boolean getApproved() {
        return (Boolean) data.get(Field.APPROVED);
    }

    /**
     *
     * @return the time at which this reservation was entered into the system
     */
    public Long getTime() {
        return (Long) data.get(Field.TIME);
    }

    /**
     *
     * @return the last time this reservation was edited
     */
    public Long getEdit() {
        return (Long) data.get(Field.EDIT);
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
     * @param approved set true if the owner approves of the reservation
     */
    public void setApproved(Boolean approved) {
        data.put(Field.APPROVED, approved);
    }

    /**
     * @return true if an admin checked the reservation
     */
    public Boolean getPending() {
        return (Boolean) data.get(Field.PENDING);
    }

    /**
     * @param pending set true if an admin checked the reservation
     */
    public void setPending(Boolean pending) {
        data.put(Field.PENDING, pending);
    }

}