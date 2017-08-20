/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.values;

/**
 * Describe the different positions of a window handle.
 *
 * At the time of writing, we are using that for the EEP F6-10-00/01.
 * This EEP does not differ between left or right, so we use a LEFT_OR_RIGHT value.
 * Perhaps a better EEP will be introduced used later, that could support to differ LEFT or RIGHT.
 * Till then, we do not introduce this values (could be added later, too).
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public enum WindowHandlePosition {

    UP, DOWN, LEFT_OR_RIGHT
}
