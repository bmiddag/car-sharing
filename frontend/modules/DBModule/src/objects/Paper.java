package objects;

import java.util.EnumMap;

/**
 * Representation of a File to prove an Insurance.
 */
public class Paper extends DataObject {

    public enum Field implements DataField {

	INSURANCE(Integer.class),
        FILE(Integer.class, true, false);

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
     * @param insurance the Insurance to which this paper belongs
     * @param file the File containing this paper
     */
    public Paper(Integer insurance, Integer file) {
        super(new EnumMap<>(Field.class));
        data.put(Field.FILE, file);
        data.put(Field.INSURANCE, insurance);
    }
    
    

    /**
     *
     * @return the File containing this paper
     */
    public Integer getFile() {
        return (Integer) data.get(Field.FILE);
    }

    /**
     *
     * @return the insurance to which this paper belongs
     */
    public Integer getInsurance() {
        return (Integer) data.get(Field.INSURANCE);
    }

}
