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

package eu.trowl.query.sparql.endpoint;

/**
 *
 * @author ed
 */
public class InvalidParameterException extends Exception {

    /**
     *
     */
    public InvalidParameterException() {
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param message
         */
        public InvalidParameterException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param cause
         */
        public InvalidParameterException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param message
         * @param cause
         */
        public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
