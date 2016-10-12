package objects;

import java.util.EnumMap;

/**
 * Insurance class containing general info. Multiple proof files link back to
 * Insurance objects via Papers.
 *
 * @see Paper
 */
public class Insurance extends DataObject {

    public enum Field implements DataField {

	ID(Integer.class, true, false),
	CAR(Car.class), 
	NUMBER(String.class), 
	COMPANY(String.class), 
	BONUSMALUS(Integer.class), 
	OMNIUM(Boolean.class), 
	EXPIRATION(java.sql.Date.class),
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
     * @param id Assigned by the DB.
     * @param car The car that is insured by this insurance, cannot be null.
     * @param company Insurance company.
     * @param number The insurance company assigned insurance number
     * @param bonusmalus The tier this car is on.
     * @param omnium Is this an omnium insurance yes/no?
     * @param expiration Date this insurance expires.
     * @param time Creation date.
     * @param edit Last edit.
     */
    public Insurance(Integer id, Car car, String company, String number,
            Integer bonusmalus, Boolean omnium, java.sql.Date expiration, Long time, Long edit) {
        
        super(new EnumMap<>(Field.class));
        
        data.put(Field.ID, id);
        data.put(Field.CAR, car);
        data.put(Field.NUMBER, number);
        data.put(Field.COMPANY, company);
        data.put(Field.BONUSMALUS, bonusmalus);
        data.put(Field.EXPIRATION, expiration);
        data.put(Field.OMNIUM, omnium);
        data.put(Field.TIME, time);
        data.put(Field.EDIT, edit);
        
    }
    
    

    /**
     * @return the DB id
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return the car this insurance insures.
     */
    public Car getCar() {
        return (Car) data.get(Field.CAR);
    }

    /**
     * @return the insurance number
     */
    public String getNumber() {
        return (String) data.get(Field.NUMBER);
    }

    /**
     * @param number the insurance number to set
     */
    public void setNumber(String number) {
        data.put(Field.NUMBER, number);
    }

    /**
     * @return the insurance company
     */
    public String getCompany() {
        return (String) data.get(Field.COMPANY);
    }

    /**
     * @param company the insurance company
     */
    public void setCompany(String company) {
        data.put(Field.COMPANY, company);
    }

    /**
     * @return the bonusmalus tier
     */
    public Integer getBonusmalus() {
        return (Integer) data.get(Field.BONUSMALUS);
    }

    /**
     * @param bonusmalus the bonusmalus tier to set
     */
    public void setBonusmalus(int bonusmalus) {
        data.put(Field.BONUSMALUS, bonusmalus);
    }

    /**
     * @return True if this insurance offers omnium insurance.
     */
    public Boolean getOmnium() {
        return (Boolean) data.get(Field.OMNIUM);
    }

    /**
     * @param omnium true if the insurance offers omnium insurance.
     */
    public void setOmnium(Boolean omnium) {
        data.put(Field.OMNIUM, omnium);
    }

    /**
     * @return the expiration date
     */
    public java.sql.Date getExpiration() {
        return (java.sql.Date) data.get(Field.EXPIRATION);
    }

    /**
     * @param expiration the expiration date to set
     */
    public void setExpiration(java.sql.Date expiration) {
        data.put(Field.EXPIRATION, expiration);
    }

    /**
     * @return the creation time
     */
    public Long getTime() {
        return (Long) data.get(Field.TIME);
    }

    /**
     * @return the time of the last edit
     */
    public Long getEdit() {
        return (Long) data.get(Field.EDIT);
    }
    
}
