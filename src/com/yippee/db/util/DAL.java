package com.yippee.db.util;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.yippee.db.model.DocAug;
import com.yippee.db.model.Hit;
import com.yippee.db.model.HitList;
import com.yippee.db.model.RobotsTxt;
import com.yippee.db.model.Word;

/**
 * Data access layer class: it provides simplified access to data stored in persistent storage (BerkeleyDB). It provides
 * a layer of abstraction to the managers
 */
public class DAL {
    // DocAug Accessors
    PrimaryIndex<String, DocAug> docById;
    // Robots Accessors
    PrimaryIndex<String, RobotsTxt> robotsById;
    // Lexicon Accessors
    PrimaryIndex<String, Word> lexiconById;
    // Barrel Accessors
    PrimaryIndex<String, HitList> barrelById;

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
}