package objects;

import java.util.EnumMap;

public class Invoice extends DataObject {

    public enum Field implements DataObject.DataField {
	
	ID(Integer.class, true, false),
	USER(User.class), 
        PDF(Integer.class),
        EXCEL(Integer.class),
        TOTALCOST(Integer.class),
	TILL(Long.class), 
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

    public Invoice(Integer id, User user, Integer pdf, Integer excel, Integer totalcost, Long till, Long time) {
        
        super(new EnumMap<>(Field.class));
        
        data.put(Field.ID, id);
        data.put(Field.USER, user);
        data.put(Field.PDF, pdf);
        data.put(Field.EXCEL, excel);
        data.put(Field.TOTALCOST, totalcost);
        data.put(Field.TILL, till);
        data.put(Field.TIME, time);
        
    }
    
    

    public Long getTillDate() {
        return (Long) data.get(Field.TILL);
    }

    public Long getTime() {
        return (Long) data.get(Field.TIME);
    }

    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    public User getUser() {
        return (User) data.get(Field.USER);
    }

    public Integer getPDF() {
        return (Integer) data.get(Field.PDF);
    }
    
    public void setPDF(Integer file) {
        data.put(Field.PDF, file);
    }

    public Integer getExcel() {
        return (Integer) data.get(Field.EXCEL);
    }
    
    public void setExcel(Integer file) {
        data.put(Field.EXCEL, file);
    }

    public Integer getTotalCost() {
        return (Integer) data.get(Field.TOTALCOST);
    }
    
    public void setTotalCost(Integer cost) {
        data.put(Field.TOTALCOST, cost);
    }
    
    public void setUser(User user) {
        data.put(Field.USER, user);
    }
    
    public void setTillDate(Long till) {
        data.put(Field.TILL, till);
    }
    
}
