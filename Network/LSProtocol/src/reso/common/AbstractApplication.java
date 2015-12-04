/*******************************************************************************
 * Copyright (c) 2011 Bruno Quoitin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Bruno Quoitin - initial API and implementation
 ******************************************************************************/
package reso.common;

public abstract class AbstractApplication
{

    protected Host host;
    public final String name;

    public AbstractApplication(Host host, String name)
    {
    	this.host= host;
    	this.name= name;
    }

    public Host getHost()
    {
    	return host;
    }

    public abstract void start()
    	throws Exception;
    
    public abstract void stop();

}
