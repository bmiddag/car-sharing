package objects;

import java.util.EnumMap;

/**
 * A class representing a sorts of costs made for a car by the owner.
 */
public class Cost extends DataObject {

    public enum Field implements DataField {

	ID(Integer.class, true, false), 
	CAR(Integer.class),
	PRICE(Integer.class), 
	TYPE(String.class), 
	PROOF(Integer.class), 
	DESCRIPTION(String.class), 
	APPROVED(Boolean.class), 
	MOMENT(Long.class),
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
     * @param id Assigned to the DB
     * @param car The car this cost was made for
     * @param price The exact price of the cost.
     * @param type Type of the cost
     * @param proof A file containing proof of the cost, usually a bill.
     * @param description Description of the cost
     * @param approved Moderator approved?
     * @param moment Time this cost was made
     * @param time Time the cost was added to the DB
     * @param edit The last time an edit was made to this Cost
     */
    public Cost(Integer id, Integer car, Integer price, String type, Integer proof,
            String description, Boolean approved, Long moment, Long time, Long edit) {
        
        super(new EnumMap<>(Field.class));

        data.put(Field.ID, id);
        data.put(Field.CAR, car);
        data.put(Field.PRICE, price);
        data.put(Field.TYPE, type);
        data.put(Field.PROOF, proof);
        data.put(Field.DESCRIPTION, description);
        data.put(Field.APPROVED, approved);
        data.put(Field.MOMENT, moment);
        data.put(Field.TIME, time);
        data.put(Field.EDIT, edit);

    }
    
    

    /**
     * @return the id assigned by the DB
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return the car
     */
    public Integer getCar() {
        return (Integer) data.get(Field.CAR);
    }

    /**
     * @return the price
     */
    public Integer getPrice() {
        return (Integer) data.get(Field.PRICE);
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Integer price) {
        data.put(Field.PRICE, price);
    }

    /**
     * @return the type of cost
     */
    public String getType() {
        return (String) data.get(Field.TYPE);
    }

    /**
     * @param type the type of cost to set
     */
    public void setType(String type) {
        data.put(Field.TYPE, type);
    }

    /**
     * @return the proof of this cost
     */
    public Integer getProof() {
        return (Integer) data.get(Field.PROOF);
    }

    /**
     * @param proof A file that proves this is a valid cost
     */
    public void setProof(Integer proof) {
        data.put(Field.PROOF, proof);
    }

    /**
     * Boolean
     *
     * @return the description of the cost
     */
    public String getDescription() {
        return (String) data.get(Field.DESCRIPTION);
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        data.put(Field.DESCRIPTION, description);
    }

    /**
     * @return Moderator approved?
     */
    public Boolean getApproved() {
        return (Boolean) data.get(Field.APPROVED);
    }

    /**
     * @param approved Is this moderator approved?
     */
    public void setApproved(Boolean approved) {
        data.put(Field.APPROVED, approved);
    }

    /**
     * @return the time this cost was entered into the DB
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

    /**
     * @return the moment
     */
    public Long getMoment() {
        return (Long) data.get(Field.MOMENT);
    }

    /**
     * @param moment the moment to set
     */
    public void setMoment(Long moment) {
        data.put(Field.MOMENT, moment);
    }
}
