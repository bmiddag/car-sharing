package interfaces;

import objects.Comment;
import objects.Damage;
import objects.User;
import exceptions.DataAccessException;
import java.util.List;

public interface CommentDAO extends DataAccessObject<Comment> {

    /**
     * Creates a new Comment with these specific parameters.
     *
     * @see objects.Comment
     * @param user Parameter for the new Comment
     * @param damage Parameter for the new Comment
     * @param content Parameter for the new Comment
     * @return A new objects.Comment object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Comment createComment(User user, Damage damage, String content) throws DataAccessException;

    /**
     * Returns the Comment with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param id The id of the Comment to get from the DB
     * @return A new objects.Comment object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Comment getComment(Integer id) throws DataAccessException;

    /**
     * Retrieves all Comments on a specific Damage case.
     *
     * @param damage the comments are about
     * @return A new objects.Comment object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Comment> getCommentsOnDamage(Damage damage) throws DataAccessException;

    /**
     * Writes all fields of this Comment to the DB.
     *
     * @param comment the Comment to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateComment(Comment comment) throws DataAccessException;

    /**
     * Deletes the specified Comment.
     *
     * @param comment The Comment to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteComment(Comment comment) throws DataAccessException;

}
