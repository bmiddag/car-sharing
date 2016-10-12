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
import interfaces.CommentDAO;
import interfaces.Filter;
import java.util.Date;
import java.util.List;
import objects.Comment;
import objects.Damage;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.User;

class JDBCCommentDAO extends JDBCDataAccessObject<Comment> implements CommentDAO {

    JDBCCommentDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Comment.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Comment.Field.TIME};
    }
    
    @Override
    protected DataField getKey() {
        return Comment.Field.ID;
    }

    @Override
    public Filter<Comment> getFilter() {
        return new JDBCFilter<Comment>() {};
    }
    
    @Override
    public Comment createComment(User user, Damage damage, String content) throws DataAccessException {
        return new Comment(create(user, damage, content), user.getId(), damage, content, new Date().getTime());
    }

    @Override
    public void updateComment(Comment comment) throws DataAccessException {
        update(comment);
    }

    @Override
    public void deleteComment(Comment comment) throws DataAccessException {
        deleteByID(comment.getId());
    }

    @Override
    public Comment getComment(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Comment> getCommentsOnDamage(Damage damage) throws DataAccessException {
        return getByAttr("damage", damage.getId());
    }
    
}
