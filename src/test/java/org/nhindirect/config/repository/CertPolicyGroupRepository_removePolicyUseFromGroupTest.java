package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupReltn;
import org.nhindirect.config.store.CertPolicyUse;
import org.nhindirect.policy.PolicyLexicon;

import edu.emory.mathcs.backport.java.util.Collections;

public class CertPolicyGroupRepository_removePolicyUseFromGroupTest extends CertPolicyDaoBaseTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void testRemovePolicyFromGroup_addedPolicyToGroup_assertAssociationRemoved()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");
		groupRepo.save(group);
		
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		CertPolicyGroupReltn reltn = new CertPolicyGroupReltn();
		reltn.setCertPolicy(policy);
		reltn.setCertPolicyGroup(group);
		reltn.setPolicyUse(CertPolicyUse.PUBLIC_RESOLVER);
		reltn.setIncoming(true);
		reltn.setOutgoing(false);
		
		group.setCertPolicyGroupReltn(Arrays.asList(reltn));
		
		groupRepo.save(group);
		
			
		CertPolicyGroup assocGroup = groupRepo.findById(group.getId()).get();
		assertEquals(1, assocGroup.getCertPolicyGroupReltn().size());
		
		group.setCertPolicyGroupReltn(Collections.emptyList());
		groupRepo.save(group);
		
		assocGroup = groupRepo.findById(group.getId()).get();
		assertEquals(0, assocGroup.getCertPolicyGroupReltn().size());		
		
	}
}
