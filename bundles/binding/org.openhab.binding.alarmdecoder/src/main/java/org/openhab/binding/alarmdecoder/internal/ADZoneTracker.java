/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarmdecoder.internal;

import org.openhab.binding.alarmdecoder.internal.model.ADZone;
import java.util.ArrayList;
import java.util.ListIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Track zone faults / restores by zone ID watching the alarm panel
 * message stream
 *
 * @author Sean Mathews <coder@f34r.com>
 * @since 1.6.0
 */
public class ADZoneTracker {

    private static final Logger logger = LoggerFactory.getLogger(ADZoneTracker.class);

    private ArrayList<ADZone> _zoneList;
    private boolean _lastReady;
    private int _lastZone = 0;

    // TODO: zone timeout handling. zone list garbage collector.
    long _currTime = 0;


    ADZoneTracker() {
        _zoneList = new ArrayList<ADZone>();
        _lastReady = false;
        _currTime = System.nanoTime();
    }

    /**
     * get the state of a zone
     *
     * @param Zone - the zone
     * @return return zone status or null if the zone is invalid
     *     -1 = unknown
     *      0 = restored
     *      1 = faulted
     */
    public boolean getZoneState(int Zone) {

        /** find the zone in our List */
        ADZone f = new ADZone(Zone);
        int z = _zoneList.indexOf(f);

        if ( z > -1 ) {
            f = _zoneList.get(z);
        }

        return f.getState();
    }

    /**
     * set the state of a zone
     *
     * @param Zone  - the zone
     * @param State - faulted or not boolean
     * @return zone status
     *     -1 = unknown
     *      0 = restored
     *      1 = faulted
     */
    public boolean setZoneState(int Zone, boolean State) {

        /** find the zone in our List */
        ADZone f = new ADZone(Zone);
        int z = _zoneList.indexOf(f);

        if ( z > -1 ) {
            f.setState(State);
            f = _zoneList.set(z,f);
        } else {
            f.setState(State);
            _zoneList.add(f);
        }

        return f.getState();
    }

    /**
     * Clear all tracked zones happens when we see a "READY" from the panel
     *
     */
     public void clearAll() {
         _zoneList.clear();
         _lastZone = 0;
         _lastReady = true;
     }

    /**
     * Update a zone state from messages from panel. Must be in the
     * correct sequence and not modified from what the panel provides
     * to properly track zones
     *
     * @param Zone  - the zone
     * @param State - the state
     * @return ArrayList of zones effected
     */
     public ArrayList updateZone(int Zone, boolean State) {

        /** return a list of update zones */
        ArrayList<Integer> updateList =  new ArrayList<Integer>();

        /** add this zone */
        updateList.add(Zone);

        /** our list iterator */
        ListIterator litr = _zoneList.listIterator();

        /** zone exists so check for any pruning */
        if(_zoneList.contains(new ADZone(Zone))) {
            /** go to our last zone updated */
            ADZone f = new ADZone(_lastZone);
            int z = _zoneList.indexOf(f);

		    boolean bFoundLast = false;

            /** look for any missing zones and clear them */
            while ( !bFoundLast ) {
               ADZone zn;

                boolean bHasNext=litr.hasNext();
                if ( bHasNext ) {
                    zn = (ADZone) litr.next();
                } else {
                    break;
                }

                /** we found it so break out */
                if( zn.getID() == _lastZone ) {
	               bFoundLast = true;
                }
            }

            /**
             * Iterate through the rest till we reach the end or find our
             * new fault adding everything we find as we go to our remove list
             */
            ArrayList<Integer> removeList =  new ArrayList<Integer>();
            boolean bFoundNew = false;
            while(litr.hasNext() && !bFoundNew) {
                ADZone zn = (ADZone) litr.next();
                if( zn.getID() == Zone ) {
                    bFoundNew=true;
                    break;
                } else {
                    removeList.add(zn.getID());
                    updateList.add(zn.getID());
                }
            }

            /**
             * start over if we didnt find the end and remove
             * everything we find till we do reach our current fault
             */
             if(!bFoundNew) {
                 litr = _zoneList.listIterator();
                 while(litr.hasNext() && !bFoundNew) {
                     ADZone zn = (ADZone) litr.next();
                     if( zn.getID() == Zone ) {
                         bFoundNew=true;
                         break;
                     } else {
                         removeList.add(zn.getID());
                         updateList.add(zn.getID());
                     }
                 }
             }

             litr = removeList.listIterator();
             while ( litr.hasNext() ) {
                 Integer zid = (Integer)litr.next();
                 ADZone zn = new ADZone(zid);
                 _zoneList.remove(zn);
             }

         /** zone does not exist just get it into our list */
         } else {

             ADZone f = new ADZone(Zone);

             boolean bInserted=false;
             int offset=0;
             litr = _zoneList.listIterator();
             while(litr.hasNext()) {
                 ADZone zn = (ADZone) litr.next();
                 if( zn.getID() > Zone ) {
                     _zoneList.add(offset, f);
                     bInserted=true;
                     break;
                 }
                 offset++;
             }

             /** just add it */
             if ( !bInserted ) {
                 _zoneList.add(f);
             }
         }

         /** update our last touched zone */
         _lastZone = Zone;

         setZoneState(Zone,State);

         return updateList;
     }

     /**
      * Update panel Ready state clear zones if needed
      *
      * @parm State - the current panel READY state
      * @return ArrayList of zones effected
      */
      public ArrayList updateReadyState(boolean State) {

          ArrayList<Integer> updateList = new ArrayList<Integer>();

          /** if we go ready clear all faults */
          if ( State && _lastReady != State ) {
              /** copy our zones to our update list */
              for (ADZone tz : _zoneList) {
                  updateList.add(tz.getID());
              }
              /** now clear the list */
              clearAll();
          }
          _lastReady = State;

          /** return a list of update zones */
          return updateList;
      }

}
