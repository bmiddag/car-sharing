package objects;

import java.util.EnumMap;

/**
 * Linking a User to a Car indicating the user can drive the car as if he was the owner.
 */
public class Privilege extends DataObject {
    
    protected DataField[] getDataFields() {
    return Field.values();
}

public enum Field implements DataField {

        ID (Integer.class, true, false),
	USER(Integer.class), 
	CAR(Integer.class);

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
     * @param user the User having the privilege
     * @param car the car on which the privilege is given
     */
    public Privilege(Integer id, Integer user, Integer car) {
        super(new EnumMap<>(Field.class));
        data.put(Field.USER, user);
        data.put(Field.CAR, car);
        data.put(Field.ID, id);
    }
    
    

    /**
     *
     * @return the id of the privilege
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     *
     * @return the user having the privilege
     */
    public Integer getUser() {
        return (Integer) data.get(Field.USER);
    }

    /**
     *
     * @return the car to be used
     */
    public Integer getCar() {
        return (Integer) data.get(Field.CAR);
    }

}
