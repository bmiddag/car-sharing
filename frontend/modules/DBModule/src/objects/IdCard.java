package objects;

import java.util.EnumMap;

/**
 * The identification card of a user.
 */
public class IdCard extends DataObject {

    public enum Field implements DataField {

	ID(Integer.class, true, false),
	NUMBER(String.class), 
	REGISTER(String.class), 
	FILE(Integer.class);

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
     * @param number The IdCard number
     * @param register The registration number
     * @param file Copy of the IdCard
     */
    public IdCard(Integer id, String number, String register, Integer file) {
        
        super(new EnumMap<>(Field.class));
        
        data.put(Field.ID, id);
        data.put(Field.NUMBER, number);
        data.put(Field.REGISTER, register);
        data.put(Field.FILE, file);

    }
    
    

    /**
     * @return the id assigned by the DB
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return the IdCard number
     */
    public String getNumber() {
        return (String) data.get(Field.NUMBER);
    }

    /**
     * @param number the IdCard number to set
     */
    public void setNumber(String number) {
        data.put(Field.NUMBER, number);
    }

    /**
     * @return the registration number
     */
    public String getRegister() {
        return (String) data.get(Field.REGISTER);
    }

    /**
     * @param register The new registration number
     */
    public void setRegister(String register) {
        data.put(Field.REGISTER, register);
    }

    /**
     * @return the scan of this IdCard
     */
    public Integer getFile() {
        return (Integer) data.get(Field.FILE);
    }

    /**
     * @param file A scan of the IdCard
     */
    public void setFile(Integer file) {
        data.put(Field.FILE, file);
    }
    
}
