/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.db.observer;

import org.telosys.tools.commons.observer.TaskObserver2;

public class DatabaseObserverProvider{

	private static Class<? extends TaskObserver2<Integer,String>> observerClass = null ;
	
	/**
	 * Private constructor
	 */
	private DatabaseObserverProvider() {
	}
	
	public static void setObserverClass(Class<? extends TaskObserver2<Integer,String>> clazz ) {
		observerClass = clazz ;
	}
	
	public static Class<? extends TaskObserver2<Integer,String>> getObserverClass() {
		return observerClass ;
	}
	
	public static TaskObserver2<Integer,String> getNewObserverInstance() {
		TaskObserver2<Integer,String> instance = null ;
		if ( observerClass != null ) {
			try {
				instance = observerClass.newInstance() ;
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalStateException("Cannot create TaskObserver instance", e);
			}
		}
		return instance ;
	}
	
}
