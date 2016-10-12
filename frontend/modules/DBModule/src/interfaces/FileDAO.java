package interfaces;

import objects.File;
import exceptions.DataAccessException;
import java.io.InputStream;

public interface FileDAO extends DataAccessObject<File> {
    
    /**
     * Creates a new File with this data.
     *
     * @see objects.File
     * @param file The file.
     * @return A new objects.File object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public File createFile(java.io.File file) throws DataAccessException;
    
    /**
     * Creates a new File with this data.
     *
     * @see objects.File
     * @param data The file.
     * @return A new objects.File object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public File createFile(InputStream data) throws DataAccessException;
    
    public File createFile(InputStream data, String filename) throws DataAccessException;
    
    public File createFile(java.io.File file, String filename) throws DataAccessException;

    /**
     * Returns the File with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param id The id of the File to get from the DB
     * @return A new objects.File object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public File getFile(Integer id) throws DataAccessException;

    /**
     * Deletes the specified File.
     *
     * @param file The File to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteFile(File file) throws DataAccessException;

    /**
     * Deletes the specified File.
     *
     * @param id The File to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteFile(Integer id) throws DataAccessException;

}
