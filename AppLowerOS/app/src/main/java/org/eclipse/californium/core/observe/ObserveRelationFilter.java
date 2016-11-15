/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * <p>
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.html.
 * <p>
 * Contributors:
 * Achim Kraus (Bosch Software Innovations GmbH) - initial version
 ******************************************************************************/
package org.eclipse.californium.core.observe;

/**
 * Define a (sub-)selection of observe relations.
 * Used by {@link org.eclipse.californium.core.CoapResource#changed(ObserveRelationFilter)}
 */
public interface ObserveRelationFilter {
    /**
     * Check, if the observe relation should be accepted by this filter.
     * @param relation observe relation
     * @return <code>true</code>, if the relation should be selected,
     *         <code>false</code>, if not
     */
    boolean accept(ObserveRelation relation);
}
