/*        ______ _____  _____            _   _                    *
 *       |  ____|  __ \|  __ \     /\   | \ | |Verantwoordelijken:*
 *       | |__  | |  | | |__) |   /  \  |  \| |                   *
 *       |  __| | |  | |  _  /   / /\ \ | . ` | Laurens De Graeve *
 *       | |____| |__| | | \ \  / ____ \| |\  |  & Wouter Termont *
 *       |______|_____/|_|  \_\/_/    \_\_|_\_|    _              *
 *       |  __ \|  _ \                    | |     | |             *
 *       | |  | | |_) |_ __ ___   ___   __| |_   _| | ___         *
 *       | |  | |  _ <| '_ ` _ \ / _ \ / _` | | | | |/ _ \        *
 *       | |__| | |_) | | | | | | (_) | (_| | |_| | |  __/        *
 *       |_____/|____/|_| |_| |_|\___/ \__,_|\__,_|_|\___|        *
 *                                                                *
 *****************************************************************/
package jdbc;

import exceptions.DataAccessException;
import interfaces.Filter;
import interfaces.TemplateDAO;
import java.util.Date;
import java.util.List;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Template;

class JDBCTemplateDAO extends JDBCDataAccessObject<Template> implements TemplateDAO {
    
    JDBCTemplateDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Template.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Template.Field.EDIT};
    }
    
    @Override
    protected DataField getKey() {
        return Template.Field.ID;
    }

    @Override
    public Filter<Template> getFilter() {
        return new JDBCFilter<Template>() {};
    }

    @Override
    public Template createTemplate(String name) throws DataAccessException {
        return createTemplate(name, "");
    }

    @Override
    public Template createTemplate(String name, String content, Boolean removable) throws DataAccessException {
        return new Template(create(name, content, removable), name, content, removable, new Date().getTime());
    }
    
    @Override
    public Template createTemplate(String name, String content) throws DataAccessException {
        return new Template(create(name, content, true), name, content, true, new Date().getTime());
    }

    @Override
    public void updateTemplate(Template template) throws DataAccessException {
        update(template);
    }

    @Override
    public void deleteTemplate(Template template) throws DataAccessException {
        deleteByID(template.getId());
    }

    @Override
    public Template getTemplate(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Template> getAllTemplates() throws DataAccessException {
        return getAll();
    }

    @Override
    public Template getTemplate(String name) throws DataAccessException {
        List<Template> list = getByAttr("name" , name);
        if(list.isEmpty()) return null;
        return list.get(0);
    }
    
}
