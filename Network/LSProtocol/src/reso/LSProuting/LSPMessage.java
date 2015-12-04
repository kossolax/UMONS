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
package reso.LSProuting;

import java.util.ArrayList;
import java.util.List;

import reso.common.Message;
import reso.ip.IPAddress;
import reso.ip.IPInterfaceAdapter;

public class LSPMessage implements Message {

	
	public final String message;
	
	public LSPMessage(String _message) {
		message = _message;
	}
	
	public String toString() {
		return message;
	}

}
