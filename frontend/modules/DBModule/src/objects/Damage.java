package objects;

import java.util.EnumMap;

/**
 * A damage case. Multiple proof files are linked via DamageDocs. Comments are linked
 * via the Comment class.
 * 
 * @see DamageDoc
 * @see Comment
 */
public class Damage extends DataObject {

    public enum Field implements DataField {

	ID(Integer.class, true, false),
	USER(Integer.class),
	CAR(Integer.class),  
	STATUS(String.class), 
	DESCRIPTION(String.class), 
	OCCURRED(Long.class),
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
     * @param id assigned by the DB
     * @param user The reporting user.
     * @param car The damaged car
     * @param status The current state of this damage case
     * @param description A description of the damage
     * @param occurred Date this damage occurred on
     * @param time Date the damage was entered into the system.
     * @param edit When was the last edit made
     */
    public Damage(Integer id, Integer user, Integer car, String status,
            String description, Long occurred, Long time, Long edit) {

        super(new EnumMap<>(Field.class));
        
        data.put(Field.ID, id);
        data.put(Field.USER, user);
        data.put(Field.CAR, car);
        data.put(Field.TIME, time);
        data.put(Field.EDIT, edit);
        data.put(Field.OCCURRED, occurred);
        data.put(Field.STATUS, status);
        data.put(Field.DESCRIPTION, description);
        
    }
    
    

    /**
     * @return the id
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return the user
     */
    public Integer getUser() {
        return (Integer) data.get(Field.USER);
    }

    /**
     * @return the car
     */
    public Integer getCar() {
        return (Integer) data.get(Field.CAR);
    }

    /**
     * @return the time
     */
    public Long getTime() {
        return (Long) data.get(Field.TIME);
    }

    /**
     * @return the status of this case
     */
    public String getStatus() {
        return (String) data.get(Field.STATUS);
    }

    /**
     * @param status the status of this case
     */
    public void setStatus(String status) {
        data.put(Field.STATUS, status);
    }

    /**
     * @return the description of the damages
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
     * @return last edit in this damage
     */
    public Long getEdit() {
        return (Long) data.get(Field.EDIT);
    }

    /**
     * @return Date of occurrence of this damage
     */
    public Long getOccurred() {
        return (Long) data.get(Field.OCCURRED);
    }

    /**
     * @param occurred Date of occurrence of this damage
     */
    public void setOccurred(Long occurred) {
        data.put(Field.OCCURRED, occurred);
    }
    
}
