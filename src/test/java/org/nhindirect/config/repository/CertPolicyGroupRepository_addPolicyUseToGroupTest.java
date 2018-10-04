package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupReltn;
import org.nhindirect.config.store.CertPolicyUse;
import org.nhindirect.policy.PolicyLexicon;

public class CertPolicyGroupRepository_addPolicyUseToGroupTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testAddPolicyToGroup_associatePolicyAndGroup_assertAssociationAdded()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group);
		
		CertPolicyGroupReltn reltn = new CertPolicyGroupReltn();
		reltn.setCertPolicy(policy);
		reltn.setCertPolicyGroup(group);
		reltn.setPolicyUse(CertPolicyUse.PUBLIC_RESOLVER);
		reltn.setIncoming(true);
		reltn.setOutgoing(false);
		
		group.setCertPolicyGroupReltn(Arrays.asList(reltn));
		
		groupRepo.save(group);
		
		final CertPolicyGroup assocGroup = groupRepo.findById(group.getId()).get();
		assertEquals(1, assocGroup.getCertPolicyGroupReltn().size());
		reltn = assocGroup.getCertPolicyGroupReltn().iterator().next();
		
		assertEquals(policy.getId(), reltn.getCertPolicy().getId());
		assertEquals(group.getId(), reltn.getCertPolicyGroup().getId());
		assertEquals(CertPolicyUse.PUBLIC_RESOLVER, reltn.getPolicyUse());
		assertTrue(reltn.isIncoming());
		assertFalse(reltn.isOutgoing());
	}
}
