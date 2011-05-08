package cz.poptavka.sample.dao.common;

import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.common.TreeItem;

import java.util.List;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 8.2.11
 */
public interface TreeItemDao {

    /**
     *
     * Get all descendants (recursively) of given <code>parentNode</code>.
     * If parent node is not specified then method returns all tree items of specified class.
     * <p>
     * Completely all children are returned, not only direct descendants!
     *
     * @param parentNode parent node whose children are loaded and returned. Can be null, then all instances of
     * entity class <code>treeItemClass</code> are returned.
     * @param treeItemClass type of children
     * @param resultCriteria additional criteria that are applied to the result, can be null
     * @return all children of <code>parentNode</code>.
     */
    <T extends TreeItem> List<T> getAllChildren(TreeItem parentNode, Class<T> treeItemClass,
                                                ResultCriteria resultCriteria);


    /**
     *  Get IDs of all tree items that match between left bound and right bound
     * of some parent tree item from <code>treeItems</code>.
     * <p>
     * This can be viewed as some kind of recursive process because all tree items that belongs to a tree structure
     * under  some tree item from <code>treeItems</code> are found.
     *
     * @param treeItems
     * @return
     */
    <T extends TreeItem> Set<Long> getAllChildItemsIdsRecursively(List<? extends TreeItem> treeItems,
                                                                  Class<T> treeItemClass);

    /**
     * Get IDs of all leaves in hierarchical structure.
     * A node in a tree is considered to be a leaf if there is no other node which has parent reference to the node.
     *
     * @return
     */
    List<Long> getAllLeavesIds(Class<? extends TreeItem> treeItemClazz);
}
