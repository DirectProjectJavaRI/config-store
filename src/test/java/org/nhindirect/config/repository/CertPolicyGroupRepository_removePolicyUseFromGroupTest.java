package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.List;

import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupReltn;
import org.nhindirect.config.store.CertPolicyUse;
import org.nhindirect.policy.PolicyLexicon;

import reactor.test.StepVerifier;

public class CertPolicyGroupRepository_removePolicyUseFromGroupTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testRemovePolicyFromGroup_addedPolicyToGroup_assertAssociationRemoved()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");
		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML.ordinal());
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroupReltn reltn = new CertPolicyGroupReltn();
		reltn.setPolicyId(policy.getId());
		reltn.setPolicyGroupId(group.getId());
		reltn.setPolicyUse(CertPolicyUse.PUBLIC_RESOLVER.ordinal());
		reltn.setIncoming(true);
		reltn.setOutgoing(false);
		
		
		groupReltRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
			
		List<CertPolicyGroupReltn> assocGroups = groupReltRepo.findByPolicyGroupId(group.getId()).collectList().block();
		assertEquals(1, assocGroups.size());
		
		groupReltRepo.deleteByPolicyGroupId(group.getId()).block();
		
		assocGroups = groupReltRepo.findByPolicyGroupId(group.getId()).collectList().block();
		assertEquals(0, assocGroups.size());		
		
	}
}
