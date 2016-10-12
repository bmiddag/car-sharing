package objects;

import java.io.InputStream;
import java.sql.Blob;
import java.util.EnumMap;

/**
 * File class representing all sorts of (proof) data stored in the db.
 */
public class File extends DataObject {

    public enum Field implements DataField {

	ID(Integer.class, true, false),
	FILENAME(String.class),
	DATA(InputStream.class), 
        TIME(Long.class, false, true);

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
     * @param filename the filename (without extension)
     * @param extension the extension of the file
     * @param data the data of the file
     * @param time creation date of this file
     */
    public File(Integer id, String filename, InputStream stream, Long time) {
        
        super(new EnumMap<>(Field.class));
        
        data.put(Field.ID, id);
        data.put(Field.FILENAME, filename);
        data.put(Field.DATA, stream);
        data.put(Field.TIME, time);
        
    }
    
    

    /**
     * @return the id assigned by the DB
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return the data of the file
     */
    public InputStream getFileAsStream() {
        return (InputStream) data.get(Field.DATA);
    }

    public String getFilename() {
        return (String) data.get(Field.FILENAME);
    }

    public void setFilename(String filename) {
        data.put(Field.FILENAME, filename);
    }

    /**
     * @return the creation date
     */
    public Long getTime() {
        return (Long) data.get(Field.TIME);
    }
    
}
