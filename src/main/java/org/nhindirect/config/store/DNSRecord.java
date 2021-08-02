/* 
 Copyright (c) 2010, Direct Project
 All rights reserved.

 Authors:
    Greg Meyer     gm2552@cerner.com
  
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
Neither the name of The Direct Project (directproject.org) nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
*/

package org.nhindirect.config.store;

import java.io.IOException;
import java.time.LocalDateTime;

import org.nhindirect.config.store.util.DNSRecordUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

/**
 * The JPA Domain class representing a DNS record.  This is a generic DNS record that can represent (in theory) any
 * DNS record type.
 * 
 * @author Greg Meyer
 * @since 1.1
 */
@Table("dnsrecord")
@Data
public class DNSRecord 
{
	@Id
    private Long id;
	private String name;
	private int type;
	private int dclass;
	private long ttl;
	private byte[] data;
	
	@Column("createTime")
    private LocalDateTime createTime = LocalDateTime.now();
    
	
	/**
	 * Converts a raw wire transfer format of a record to a DNS record.
	 * @param data  The raw byte stream of a record in wire transfer format.
	 * @return A DNSRecord converted from the wire format.
	 * @throws IOException
	 */
	public static DNSRecord fromWire(byte[] data) throws IOException
	{
		return DNSRecordUtils.fromWire(data);
	}
	
	/**
	 * Converts a DNS record to a raw wire transfer format.
	 * @param rec The DNSRecord to convert.
	 * @return A byte array representation of the DNSRecord in raw wire transfer format.
	 * @throws IOException
	 */
	public static byte[] toWire(DNSRecord rec) throws IOException
	{
		return DNSRecordUtils.toWire(rec);
	}
}
