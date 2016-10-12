package controllers;

import exceptions.DataAccessException;
import interfaces.DataAccessContext;
import objects.File;
import jdbc.JDBCDataAccessProvider;
import play.mvc.Controller;

import play.mvc.Result;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Access database blob as file by rendering.
 */
public class FileRenderer extends Controller {

	/**
	 * Renders file for given id.
	 */
    public static Result getFile(Integer id) {
        
        if (id == null) return notFound();

        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            File file = dac.getFileDAO().getFile((int)id);
            if (file == null) return notFound();
            else {
                // response.setContentType(type); // OF as-methode in return
                response().setHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
                return ok(file.getFileAsStream())/*.as([content-type])*/;
            }
        } catch (DataAccessException ex) {
            return notFound();
        }

    }
}
