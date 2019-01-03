package gnu.chu.controles;

import  java.beans.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.border.*;
import javax.accessibility.*;

/**
 * The default model for combo boxes.
 *
 * @version 1.5 08/26/98
 * @author Arnaud Weber
 * @author Tom Santos
*  ... NO USADO ... PENDIENTE DE BORRAR.
 */

public class CComboBoxModel extends AbstractListModel implements MutableComboBoxModel, Serializable
{
    Vector objects;
    Object selectedObject;

    /**
     * Constructs an empty DefaultComboBoxModel object.
     */
    public CComboBoxModel() {
        objects = new Vector();
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an array of objects.
     *
     * @param items  an array of Object objects
     */
    public CComboBoxModel(final Object items[]) {
        objects = new Vector();
	objects.ensureCapacity( items.length );

        int i,c;
	for ( i=0,c=items.length;i<c;i++ )
	    objects.addElement(items[i]);

	if ( getSize() > 0 ) {
	    selectedObject = getElementAt( 0 );
	}
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector.
     *
     * @param v  a Vector object ...
     */
    public CComboBoxModel(Vector v) {
	objects = v;

	if ( getSize() > 0 ) {
	    selectedObject = getElementAt( 0 );
	}
    }

    // implements javax.swing.ComboBoxModel
    public void setSelectedItem(Object anObject) {
        selectedObject = anObject;
	fireContentsChanged(this, -1, -1);
    }

    // implements javax.swing.ComboBoxModel
    public Object getSelectedItem() {
        return selectedObject;
    }

    // implements javax.swing.ListModel
    public int getSize() {
        return objects.size();
    }

    // implements javax.swing.ListModel
    public Object getElementAt(int index) {
        if ( index >= 0 && index < objects.size() )
	    return objects.elementAt(index);
	else
	    return null;
    }

    /**
     * Returns the index-position of the specified object in the list.
     *
     * @param anObject
     * @return an int representing the index position, where 0 is
     *         the first position
     */
    public int getIndexOf(Object anObject) {
        return objects.indexOf(anObject);
    }

    // implements javax.swing.MutableComboBoxModel
    public void addElement(Object anObject) {
        objects.addElement(anObject);
    }

    public void avisaAdd()
    {
      if (objects.size() == 0)
        return;
    	fireIntervalAdded(this,objects.size()-1, objects.size()-1);
    }
    // implements javax.swing.MutableComboBoxModel
    public void insertElementAt(Object anObject,int index) {
        objects.insertElementAt(anObject,index);
	fireIntervalAdded(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    public void removeElementAt(int index) {
        if ( getElementAt( index ) == selectedObject ) {
	    if ( index == 0 ) {
	        setSelectedItem( getSize() == 1 ? null : getElementAt( index + 1 ) );
	    }
	    else {
	        setSelectedItem( getElementAt( index - 1 ) );
	    }
	}

	objects.removeElementAt(index);

	fireIntervalRemoved(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
	if ( index != -1 ) {
	    removeElementAt(index);
	}
    }

    /**
     * Empties the list.
     */
    public void removeAllElements() {
        int firstIndex = 0;
	int lastIndex = objects.size()-1;
	objects.removeAllElements();
	selectedObject = null;
	fireIntervalRemoved(this, firstIndex, lastIndex);
    }
}
