/*
 * This file is part of TrOWL.
 *
 * TrOWL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrOWL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

package eu.trowl;

/**
 *
 * @author ethomas
 */
public class QueryException extends Exception {

    /**
     * Creates a new instance of <code>QueryException</code> without detail message.
     */
    public QueryException() {
    }


    /**
     * Constructs an instance of <code>QueryException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public QueryException(String msg) {
        super(msg);
    }
}
