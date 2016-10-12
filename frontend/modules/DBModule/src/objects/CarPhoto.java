package objects;

import java.util.EnumMap;

/**
 * Photo of a car. Proved by a File.
 */
public class CarPhoto extends DataObject {

    public enum Field implements DataField {

	CAR(Integer.class),
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
     * @param car car's id
     * @param file File as proof of license
     */
    public CarPhoto(Integer car, Integer file) {
        super(new EnumMap<>(Field.class));
        data.put(Field.CAR, car);
        data.put(Field.FILE, file);
    }

    /**
     *
     * @return the id assigned by the db
     */
    public Integer getCar() {
        return (Integer) data.get(Field.CAR);
    }

    /**
     *
     * @return the proof File
     */
    public Integer getFile() {
        return (Integer) data.get(Field.FILE);
    }

}
