package objects;

import java.util.EnumMap;

/**
 * Linking users with his permission(s)
 */
public class UserPermission extends DataObject {

    public enum Field implements DataField {

	ID(Integer.class, true, false),
	USER(User.class),
	PERMISSION(Permission.class);

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
     * @param id the id assigned by the db
     * @param user the user that has this permission
     * @param permission the permission of the user
     */
    public UserPermission(Integer id, User user, Permission permission) {
        super(new EnumMap<>(Field.class));
        data.put(Field.ID, id);
        data.put(Field.USER, user);
        data.put(Field.PERMISSION, permission);

    }
    
    

    /**
     *
     * @return the id assigned to the user by the db
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     *
     * @return the user who has the permission
     */
    public User getUser() {
        return (User) data.get(Field.USER);
    }

    /**
     *
     * @return the permission of the user
     */
    public Permission getPermission() {
        return (Permission) data.get(Field.PERMISSION);
    }

    /**
     *
     * @param user the user who has the permission
     */
    public void setUser(User user) {
        data.put(Field.USER, user);
    }

    /**
     *
     * @param permission the permission of the user
     */
    public void setPermission(Permission permission) {
        data.put(Field.PERMISSION, permission);
    }

}
