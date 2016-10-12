package interfaces;

import objects.Template;
import exceptions.DataAccessException;
import java.util.List;

/**
 * This DAO Regulates access to the Templates in the database.
 */
public interface TemplateDAO extends DataAccessObject<Template> {

    /**
     * 
     * @param name the name of the new template
     * @return a Template object representing the new template in the db
     * @throws DataAccessException 
     */
    public Template createTemplate(String name) throws DataAccessException;
    public Template createTemplate(String name, String content) throws DataAccessException;
    public Template createTemplate(String name, String content, Boolean removable) throws DataAccessException;

    /**
     *
     * @param id the id of the Template to find in the db
     * @return a Template object representing the Template in the db; null if not found
     * @throws DataAccessException
     */
    public Template getTemplate(Integer id) throws DataAccessException;
    
    /**
     *
     * @param name the name of the Template to find in the db
     * @return a Template object representing the Template in the db; null if not found
     * @throws DataAccessException
     */
    public Template getTemplate(String name) throws DataAccessException;

    /**
     *
     * @param template an adapted Template object to update in the db
     * @throws DataAccessException
     */
    public void updateTemplate(Template template) throws DataAccessException;

    /**
     *
     * @param template the Template object to delete from the db
     * @throws DataAccessException
     */
    public void deleteTemplate(Template template) throws DataAccessException;

    /**
     * 
     * @return a list of all templates in the db
     * @throws DataAccessException 
     */
    public List<Template> getAllTemplates() throws DataAccessException;

}
