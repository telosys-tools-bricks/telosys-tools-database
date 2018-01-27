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

	private static Class<? extends TaskObserver2<Integer,String>> metadataObserverClass = null ;

	private static Class<? extends TaskObserver2<Integer,String>> modelObserverClass = null ;
	
	/**
	 * Private constructor
	 */
	private DatabaseObserverProvider() {
	}

	//---------------------------------------------------------------------------------------------
	// MODEL OBSERVER
	//---------------------------------------------------------------------------------------------
	public static void setModelObserverClass(Class<? extends TaskObserver2<Integer,String>> clazz ) {
		modelObserverClass = clazz ;
	}
	
	public static Class<? extends TaskObserver2<Integer,String>> getModelObserverClass() {
		return modelObserverClass ;
	}
	
	public static TaskObserver2<Integer,String> getNewModelObserverInstance() {
		TaskObserver2<Integer,String> instance = null ;
		if ( modelObserverClass != null ) {
			try {
				instance = modelObserverClass.newInstance() ;
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalStateException("Cannot create TaskObserver instance", e);
			}
		}
		return instance ;
	}
	
	//---------------------------------------------------------------------------------------------
	// METADATA OBSERVER
	//---------------------------------------------------------------------------------------------
	public static void setMetadataObserverClass(Class<? extends TaskObserver2<Integer,String>> clazz ) {
		metadataObserverClass = clazz ;
	}
	
	public static Class<? extends TaskObserver2<Integer,String>> getMetadataObserverClass() {
		return metadataObserverClass ;
	}
	
	public static TaskObserver2<Integer,String> getNewMetadataObserverInstance() {
		TaskObserver2<Integer,String> instance = null ;
		if ( metadataObserverClass != null ) {
			try {
				instance = metadataObserverClass.newInstance() ;
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalStateException("Cannot create TaskObserver instance", e);
			}
		}
		return instance ;
	}
	
}
