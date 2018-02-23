/**
 * @file Node.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models.FirebaseHelper;

/**
 * Node : Represent a interface for all leafs in FireBase database.
 */
public interface Node
{
	boolean save(Object object); // save or update object in database.
	boolean remove(Object object); // remove object from database.
}
