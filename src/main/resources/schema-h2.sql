CREATE TABLE IF NOT EXISTS certificate ( id bigint PRIMARY KEY AUTO_INCREMENT, owner VARCHAR(100) NOT NULL, thumbprint VARCHAR(100) NOT NULL, certificateData blob NOT NULL,
createTime timestamp DEFAULT NULL, validStartDate timestamp DEFAULT NULL, validEndDate timestamp DEFAULT NULL, status int,
privateKey boolean);
				
CREATE TABLE IF NOT EXISTS anchor (id bigint NOT NULL AUTO_INCREMENT, certificateId int NOT NULL, createTime timestamp DEFAULT NULL,
certificateData blob, forIncoming boolean DEFAULT NULL, forOutgoing boolean DEFAULT NULL, 
owner varchar(255) DEFAULT NULL, status int DEFAULT NULL, 
thumbprint varchar(255) DEFAULT NULL, validEndDate timestamp DEFAULT NULL,
validStartDate timestamp DEFAULT NULL, PRIMARY KEY (id));
				
CREATE TABLE IF NOT EXISTS dnsrecord (id bigint NOT NULL AUTO_INCREMENT, 
createTime timestamp DEFAULT NULL, data blob, dclass int DEFAULT NULL,
name varchar(255) DEFAULT NULL, ttl bigint DEFAULT NULL,  
type int DEFAULT NULL, PRIMARY KEY (id));
				
CREATE TABLE IF NOT EXISTS setting (id bigint NOT NULL AUTO_INCREMENT, 
createTime timestamp DEFAULT NULL, name varchar(255) DEFAULT NULL,
status int DEFAULT NULL, updateTime timestamp DEFAULT NULL,
`value` varchar(4096) DEFAULT NULL, PRIMARY KEY (id));

CREATE UNIQUE INDEX IF NOT EXISTS UKbk4oycm648x0ox633r4m22b7d ON setting(name);
				
CREATE TABLE IF NOT EXISTS domain ( id bigint NOT NULL AUTO_INCREMENT, 
createTime timestamp DEFAULT NULL, domainName varchar(255) DEFAULT NULL,
postmasterAddressId bigint DEFAULT NULL, status int DEFAULT NULL,  
updateTime timestamp DEFAULT NULL,  PRIMARY KEY (id));

CREATE UNIQUE INDEX IF NOT EXISTS UK_4qyl85kb0l95n82ouqir44d0x on domain(domainName);
				
CREATE TABLE IF NOT EXISTS address (id bigint NOT NULL AUTO_INCREMENT, createTime timestamp DEFAULT NULL,
displayname varchar(100) DEFAULT NULL, emailaddress varchar(400) DEFAULT NULL, 
endpoint varchar(255) DEFAULT NULL, status int DEFAULT NULL, 
type varchar(64) DEFAULT NULL, updateTime timestamp DEFAULT NULL,
domainId bigint NOT NULL, PRIMARY KEY (id), 
CONSTRAINT FK_3au2yxghx7hhsf0vchv1xd3kn FOREIGN KEY (domainId) REFERENCES domain (id));
CREATE INDEX IF NOT EXISTS FK_3au2yxghx7hhsf0vchv1xd3kn ON address(domainId);
				
CREATE TABLE IF NOT EXISTS trustbundle (id bigint NOT NULL AUTO_INCREMENT, bundleName varchar(255) NOT NULL, 
bundleURL varchar(255) NOT NULL, getCheckSum varchar(255) NOT NULL, createTime timestamp NOT NULL,
lastRefreshAttempt timestamp DEFAULT NULL, lastRefreshError int DEFAULT NULL,
lastSuccessfulRefresh timestamp DEFAULT NULL, refreshInterval int DEFAULT NULL,
signingCertificateData blob, PRIMARY KEY (id));

CREATE UNIQUE INDEX IF NOT EXISTS UK_7wjl5k4628iitl72bqlq6c2i9 ON trustbundle (bundleName);
				
CREATE TABLE IF NOT EXISTS trustbundleanchor (id bigint NOT NULL AUTO_INCREMENT, anchorData blob NOT NULL, 
thumbprint varchar(255) NOT NULL, validEndDate timestamp NOT NULL, validStartDate timestamp NOT NULL,
trustBundleId bigint NOT NULL, PRIMARY KEY (id), 
CONSTRAINT FK_fugf20hpqpvtj7tmmj5e7y1od FOREIGN KEY (trustBundleId) REFERENCES trustbundle (id));
CREATE INDEX IF NOT EXISTS FK_fugf20hpqpvtj7tmmj5e7y1od ON trustbundleanchor(trustBundleId);
			    
CREATE TABLE IF NOT EXISTS trustbundledomainreltn (id bigint NOT NULL AUTO_INCREMENT, forIncoming boolean DEFAULT NULL,
forOutgoing boolean DEFAULT NULL, domain_id bigint NOT NULL, trust_bundle_id bigint NOT NULL, 
PRIMARY KEY (id), CONSTRAINT FK_j52ek3h4x9e1ngct3ovs6mcp2 FOREIGN KEY (domain_id) REFERENCES domain (id), 
CONSTRAINT FK_my2vuc5a9pmw3ilxm4yphyk42 FOREIGN KEY (trust_bundle_id) REFERENCES trustbundle (id));
CREATE INDEX IF NOT EXISTS FK_j52ek3h4x9e1ngct3ovs6mcp2 on trustbundledomainreltn(domain_id);
CREATE INDEX IF NOT EXISTS FK_my2vuc5a9pmw3ilxm4yphyk42 on trustbundledomainreltn(trust_bundle_id);
			    
CREATE TABLE IF NOT EXISTS certpolicy (id bigint NOT NULL AUTO_INCREMENT, createTime timestamp NOT NULL,
lexicon int NOT NULL, data blob NOT NULL, policyName varchar(255) DEFAULT NULL, 
PRIMARY KEY (id));

CREATE UNIQUE INDEX IF NOT EXISTS UK_gxnmqpsot5r835vgl888kgul8 on certpolicy (policyName);
			   
CREATE TABLE IF NOT EXISTS certpolicygroup (id bigint NOT NULL AUTO_INCREMENT, createTime timestamp NOT NULL,
policyGroupName varchar(255) DEFAULT NULL, PRIMARY KEY (id));

CREATE UNIQUE INDEX IF NOT EXISTS UK_c749eoa4ewcyqou5270tj7r04 ON certpolicygroup (policyGroupName);
			    
CREATE TABLE IF NOT EXISTS certpolicygroupreltn (id bigint NOT NULL AUTO_INCREMENT, incoming boolean DEFAULT NULL, 
outgoing boolean DEFAULT NULL, policyUse int NOT NULL, certPolicyId bigint NOT NULL, 
certPolicyGroupId bigint NOT NULL, PRIMARY KEY (id), 
CONSTRAINT FK_5454nqfoh7rmr70sp00dnaypn FOREIGN KEY (certPolicyGroupId) REFERENCES certpolicygroup (id), 
CONSTRAINT FK_7mqvitrm37ns7uq4l47h2j9ea FOREIGN KEY (certPolicyId) REFERENCES certpolicy (id));
CREATE INDEX IF NOT EXISTS FK_7mqvitrm37ns7uq4l47h2j9ea on certpolicygroupreltn(certPolicyId);
CREATE INDEX IF NOT EXISTS FK_5454nqfoh7rmr70sp00dnaypn on certpolicygroupreltn(certPolicyGroupId);

CREATE TABLE IF NOT EXISTS certpolicygroupdomainreltn (id bigint NOT NULL AUTO_INCREMENT, policy_group_id bigint NOT NULL, 
domain_id bigint NOT NULL, PRIMARY KEY (id), 
CONSTRAINT FK_27gs35dndd40o2d4al2c44rk FOREIGN KEY (domain_id) REFERENCES domain (id), 
CONSTRAINT FK_fu24ageaudsxa3h34779e9xhw FOREIGN KEY (policy_group_id) REFERENCES certpolicygroup (id));
CREATE INDEX IF NOT EXISTS FK_fu24ageaudsxa3h34779e9xhw on certpolicygroupdomainreltn(policy_group_id);
CREATE INDEX IF NOT EXISTS FK_27gs35dndd40o2d4al2c44rk on certpolicygroupdomainreltn(domain_id);