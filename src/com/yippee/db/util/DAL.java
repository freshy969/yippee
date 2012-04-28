package com.yippee.db.util;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.db.crawler.model.FrontierSavedState;
import com.yippee.db.crawler.model.RobotsTxt;
import com.yippee.db.indexer.model.HitList;
import com.yippee.db.indexer.model.Word;
import com.yippee.db.pastry.model.NodeState;

import org.apache.log4j.Logger;

/**
 * Data access layer class: it provides simplified access to data stored in persistent storage (BerkeleyDB). It provides
 * a layer of abstraction to the managers
 */
public class DAL {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(DAL.class);
    /**
     *DocAug Accessors
     */
    PrimaryIndex<String, DocAug> docById;
    /**
     * Robots Accessors
     */
    PrimaryIndex<String, RobotsTxt> robotsById;
    /**
     * Lexicon Accessors
     */
    PrimaryIndex<String, Word> lexiconById;

    // Barrel Accessors
    PrimaryIndex<String, HitList> barrelById;
    
    // Anchors Accessors
    PrimaryIndex<String, HitList> anchorById;
    
    /**
     * FrontierSavedState Accessor
     */
    PrimaryIndex<Integer, FrontierSavedState> frontierSavedStateByVersion;
	
    /**
     * Pastry node saved state accessor
     */
    PrimaryIndex<Integer, NodeState> nodeStateByVersion;

    /**
     * Data access layer constructor
     *
     * @param store the BerkeleyDB Entity store managing persistent entity objects.
     * @throws DatabaseException
     */
    public DAL(EntityStore store) throws DatabaseException {
        // Primary key for Inventory classes
        docById = store.getPrimaryIndex(String.class, DocAug.class);
        // Robots key
        robotsById = store.getPrimaryIndex(String.class, RobotsTxt.class);
        // Lexicon key
        lexiconById = store.getPrimaryIndex(String.class, Word.class);
        // Barrel key
        barrelById = store.getPrimaryIndex(String.class, HitList.class);
        // Anchor key
        anchorById = store.getPrimaryIndex(String.class, HitList.class);
        //Frontier index
        frontierSavedStateByVersion = store.getPrimaryIndex(Integer.class, FrontierSavedState.class);
        //Pastry Node Saved State Index
        nodeStateByVersion = store.getPrimaryIndex(Integer.class, NodeState.class);
    }

    /**
     * We need a getter to access the DocAug Primary index outside the package.
     *
     * @return the userByName index
     */
    public PrimaryIndex<String, DocAug> getDocById() {
        return docById;
    }

    /**
     * Get the next object from the entity cursor
     *
     * @return the entity cursor
     */
    public EntityCursor<DocAug> getDocCursor() {
        return docById.entities();
    }
    
    /**
     * Get a cursor for the Robots Index
     * @return RobotsTxt entity cursor
     */
    public EntityCursor<RobotsTxt> getRobotsCursor(){
    	return robotsById.entities();
    }

    /**
     * Access RobotsTXT primary index outside package.
     *
     * @return the robotsTxt index
     */
    public PrimaryIndex<String, RobotsTxt> getRobotsById() {
        return robotsById;
    }
    
    /**
     * Get a cursor for the Lexicon Index
     * @return Word entity cursor
     */
    public EntityCursor<Word> getLexiconCursor(){
    	return lexiconById.entities();
    }

    /**
     * Access Lexicon primary index outside package.
     *
     * @return the Lexicon index
     */
    public PrimaryIndex<String, Word> getLexiconById() {
        return lexiconById;
    }
    
    /**
     * Get a cursor for the Barrel Index
     * @return HitList entity cursor
     */
    public EntityCursor<HitList> getBarrelCursor(){
    	return barrelById.entities();
    }

    /**
     * Access Barrel primary index outside package.
     *
     * @return the Barrel index
     */
    public PrimaryIndex<String, HitList> getBarrelById() {
        return barrelById;
    }
    
    /**
     * Get a cursor for the Anchor Index
     * @return HitList entity cursor
     */
    public EntityCursor<HitList> getAnchorCursor(){
    	return anchorById.entities();
    }

    /**
     * Access Barrel primary index outside package.
     *
     * @return the Barrel index
     */
    public PrimaryIndex<String, HitList> getAnchorById() {
        return anchorById;
    }

	public PrimaryIndex<Integer, FrontierSavedState> getFrontierStateByVersion() {
		// TODO Auto-generated method stub
		return frontierSavedStateByVersion;
	}

	public PrimaryIndex<Integer, NodeState> getNodeStateByVersion() {
		// TODO Auto-generated method stub
		return nodeStateByVersion;
	}
	
}