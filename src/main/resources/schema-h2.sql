CREATE TABLE IF NOT EXISTS certificate ( id SERIAL PRIMARY KEY, owner VARCHAR(100) NOT NULL, thumbprint VARCHAR(100) NOT NULL, certificateData longblob NOT NULL,
createTime datetime DEFAULT NULL, validStartDate datetime DEFAULT NULL, validEndDate datetime DEFAULT NULL, status int, 
privateKey bit(1));
				
CREATE TABLE IF NOT EXISTS anchor (id int NOT NULL AUTO_INCREMENT, certificateId int NOT NULL, createTime datetime DEFAULT NULL, 
certificateData longblob, forIncoming bit(1) DEFAULT NULL, forOutgoing bit(1) DEFAULT NULL, 
owner varchar(255) DEFAULT NULL, status int(11) DEFAULT NULL, 
thumbprint varchar(255) DEFAULT NULL, validEndDate datetime DEFAULT NULL, 
validStartDate datetime DEFAULT NULL, PRIMARY KEY (id));
				
CREATE TABLE IF NOT EXISTS dnsrecord (id bigint(20) NOT NULL AUTO_INCREMENT, 
createTime datetime DEFAULT NULL, data longblob, dclass int(11) DEFAULT NULL, 
name varchar(255) DEFAULT NULL, ttl bigint(20) DEFAULT NULL,  
type int(11) DEFAULT NULL, PRIMARY KEY (id));
				
CREATE TABLE IF NOT EXISTS setting (id bigint(20) NOT NULL AUTO_INCREMENT, 
createTime datetime DEFAULT NULL, name varchar(255) DEFAULT NULL, 
status int(11) DEFAULT NULL, updateTime datetime DEFAULT NULL, 
value varchar(4096) DEFAULT NULL, PRIMARY KEY (id), 
UNIQUE KEY UKbk4oycm648x0ox633r4m22b7d (name));
				
CREATE TABLE IF NOT EXISTS domain ( id bigint(20) NOT NULL AUTO_INCREMENT, 
createTime datetime DEFAULT NULL, domainName varchar(255) DEFAULT NULL, 
postmasterAddressId bigint(20) DEFAULT NULL, status int(11) DEFAULT NULL,  
updateTime datetime DEFAULT NULL,  PRIMARY KEY (id), 
UNIQUE KEY UK_4qyl85kb0l95n82ouqir44d0x (domainName));
				
CREATE TABLE IF NOT EXISTS address (id bigint(20) NOT NULL AUTO_INCREMENT, createTime datetime DEFAULT NULL, 
displayname varchar(100) DEFAULT NULL, emailaddress varchar(400) DEFAULT NULL, 
endpoint varchar(255) DEFAULT NULL, status int(11) DEFAULT NULL, 
type varchar(64) DEFAULT NULL, updateTime datetime DEFAULT NULL, 
domainId bigint(20) NOT NULL, PRIMARY KEY (id), 
CONSTRAINT FK_3au2yxghx7hhsf0vchv1xd3kn FOREIGN KEY (domainId) REFERENCES domain (id));
CREATE INDEX IF NOT EXISTS FK_3au2yxghx7hhsf0vchv1xd3kn ON address(domainId);
				
CREATE TABLE IF NOT EXISTS trustbundle (id bigint(20) NOT NULL AUTO_INCREMENT, bundleName varchar(255) NOT NULL, 
bundleURL varchar(255) NOT NULL, getCheckSum varchar(255) NOT NULL, createTime datetime NOT NULL, 
lastRefreshAttempt datetime DEFAULT NULL, lastRefreshError int(11) DEFAULT NULL, 
lastSuccessfulRefresh datetime DEFAULT NULL, refreshInterval int(11) DEFAULT NULL, 
signingCertificateData longblob, PRIMARY KEY (id), 
UNIQUE KEY UK_7wjl5k4628iitl72bqlq6c2i9 (bundleName));
				
CREATE TABLE IF NOT EXISTS trustbundleanchor (id bigint(20) NOT NULL AUTO_INCREMENT, anchorData longblob NOT NULL, 
thumbprint varchar(255) NOT NULL, validEndDate datetime NOT NULL, validStartDate datetime NOT NULL, 
trustBundleId bigint(20) NOT NULL, PRIMARY KEY (id), 
CONSTRAINT FK_fugf20hpqpvtj7tmmj5e7y1od FOREIGN KEY (trustBundleId) REFERENCES trustbundle (id));
CREATE INDEX IF NOT EXISTS FK_fugf20hpqpvtj7tmmj5e7y1od ON trustbundleanchor(trustBundleId);
			    
CREATE TABLE IF NOT EXISTS trustbundledomainreltn (id bigint(20) NOT NULL AUTO_INCREMENT, forIncoming bit(1) DEFAULT NULL,
forOutgoing bit(1) DEFAULT NULL, domain_id bigint(20) NOT NULL, trust_bundle_id bigint(20) NOT NULL, 
PRIMARY KEY (id), CONSTRAINT FK_j52ek3h4x9e1ngct3ovs6mcp2 FOREIGN KEY (domain_id) REFERENCES domain (id), 
CONSTRAINT FK_my2vuc5a9pmw3ilxm4yphyk42 FOREIGN KEY (trust_bundle_id) REFERENCES trustbundle (id));
CREATE INDEX IF NOT EXISTS FK_j52ek3h4x9e1ngct3ovs6mcp2 on trustbundledomainreltn(domain_id);
CREATE INDEX IF NOT EXISTS FK_my2vuc5a9pmw3ilxm4yphyk42 on trustbundledomainreltn(trust_bundle_id);
			    
CREATE TABLE IF NOT EXISTS certpolicy (id bigint(20) NOT NULL AUTO_INCREMENT, createTime datetime NOT NULL,
lexicon int(11) NOT NULL, data longblob NOT NULL, policyName varchar(255) DEFAULT NULL, 
PRIMARY KEY (id), UNIQUE KEY UK_gxnmqpsot5r835vgl888kgul8 (policyName));
			   
CREATE TABLE IF NOT EXISTS certpolicygroup (id bigint(20) NOT NULL AUTO_INCREMENT, createTime datetime NOT NULL, 
policyGroupName varchar(255) DEFAULT NULL, PRIMARY KEY (id), 
UNIQUE KEY UK_c749eoa4ewcyqou5270tj7r04 (policyGroupName));
			    
CREATE TABLE IF NOT EXISTS certpolicygroupreltn (id bigint(20) NOT NULL AUTO_INCREMENT, incoming bit(1) DEFAULT NULL, 
outgoing bit(1) DEFAULT NULL, policyUse int(11) NOT NULL, certPolicyId bigint(20) NOT NULL, 
certPolicyGroupId bigint(20) NOT NULL, PRIMARY KEY (id), 
CONSTRAINT FK_5454nqfoh7rmr70sp00dnaypn FOREIGN KEY (certPolicyGroupId) REFERENCES certpolicygroup (id), 
CONSTRAINT FK_7mqvitrm37ns7uq4l47h2j9ea FOREIGN KEY (certPolicyId) REFERENCES certpolicy (id));
CREATE INDEX IF NOT EXISTS FK_7mqvitrm37ns7uq4l47h2j9ea on certpolicygroupreltn(certPolicyId);
CREATE INDEX IF NOT EXISTS FK_5454nqfoh7rmr70sp00dnaypn on certpolicygroupreltn(certPolicyGroupId);

CREATE TABLE IF NOT EXISTS certpolicygroupdomainreltn (id bigint(20) NOT NULL AUTO_INCREMENT, policy_group_id bigint(20) NOT NULL, 
domain_id bigint(20) NOT NULL, PRIMARY KEY (id), 
CONSTRAINT FK_27gs35dndd40o2d4al2c44rk FOREIGN KEY (domain_id) REFERENCES domain (id), 
CONSTRAINT FK_fu24ageaudsxa3h34779e9xhw FOREIGN KEY (policy_group_id) REFERENCES certpolicygroup (id));
CREATE INDEX IF NOT EXISTS FK_fu24ageaudsxa3h34779e9xhw on certpolicygroupdomainreltn(policy_group_id);
CREATE INDEX IF NOT EXISTS FK_27gs35dndd40o2d4al2c44rk on certpolicygroupdomainreltn(domain_id);